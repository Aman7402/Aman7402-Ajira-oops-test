import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

class Wall {
    private String direction;
    private int height;
    private int intensity;

    public Wall(String  direction, int height) {
        this.direction = direction;
        this.height = height;
        this.intensity = 1;
    }

    public String getDirection() {
        return direction;
    }

    public int getHeight() {
        return height;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

class Kingdom {
    HashMap<String, Wall> allWalls;
    AttackDay d;

    public Kingdom() {
        allWalls = new HashMap<>();
        d = new AttackDay();
    }

    public void setAttackDay(String tribe, String direction, int strength) {
        this.d.setStrength(strength);
        this.d.setTribe(tribe);
        if (allWalls.containsKey(direction)) {
            Wall w = allWalls.get(direction);
            this.d.setAttackedWall(w);
        } else {
            Wall w = new Wall(direction, 0);
            allWalls.put(direction, w);
            this.d.setAttackedWall(w);
        }
    }

    public boolean attackOnWall() {
        if (d.succefulAttcak()) {
            return true;
        }
        return false;
    }
//updating wall strength
    public void updateWallStrength(String direction, int strength) {
        Wall w = allWalls.get(direction);
        w.setHeight(strength);
    }
//updating wall intensity
    public void updateWallIntensity() {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println("Enter the wall direction for which intensity has to be changed / Enter X to exist");
            String direction = scn.nextLine();
            if (direction.equals("X")) {
                break;
            }
            System.out.println("Enter the intensity");
            int intensity = Integer.valueOf(scn.nextLine());

            if (allWalls.containsKey(direction)) {
                Wall w = allWalls.get(direction);
                w.setIntensity(intensity);
            } else {
                Wall w = new Wall(direction, 0);
                w.setIntensity(intensity);
                allWalls.put(direction, w);
            }
        }

    }
}


class AttackDay {
    private Wall attackedWall;
    private String tribe;
    private int strength;

    public void setTribe(String  tribe) {
        this.tribe = tribe;
    }

    public void setAttackedWall(Wall attackedWall) {
        this.attackedWall = attackedWall;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean succefulAttcak() {
        if (attackedWall.getHeight() * attackedWall.getIntensity() < strength) {
            return true;
        }
        return false;
    }
}

class inputClass{
    private String inputStr;

    public void getInput() {
        Scanner scn = new Scanner(System.in);
        inputStr = scn.nextLine();
    }

    public boolean successSingleAttack(String attackString,
                                        Kingdom kingdom,
                                        ArrayList<String> wallsAttacked,
                                        HashMap<String, Integer> maxStrength) {

        String[] individualInput = attackString.split(" - ");
        String tribe = individualInput[0].trim().toLowerCase();
        String direction = individualInput[1].trim().toUpperCase();
        int strength = Integer.valueOf(individualInput[3].trim().toLowerCase());
        kingdom.setAttackDay(tribe, direction, strength);

        //keeping track of intensity of bigger attack
        if (maxStrength.containsKey(direction) && maxStrength.get(direction) < strength) {
            maxStrength.put(direction, strength);
        } else {
            maxStrength.put(direction, strength);
            wallsAttacked.add(direction);
        }

        if (kingdom.attackOnWall()) {
            return true;
        }
        return false;
    }
//calculating successful attack
    public int calculateSuccesfulAttack() {
        String[] arrString = inputStr.split(";");
        int noOfDays = arrString.length;
        int successfullAttack = 0;
        Kingdom kingdom = new Kingdom();

        kingdom.updateWallIntensity();

        for (int i = 0; i < noOfDays; i++) {
            String[] noOfAttacks = arrString[i].split(":");
            HashMap<String, Integer> maxStrength = new HashMap<>();
            ArrayList<String> wallsAttacked = new ArrayList<>();
            for (int j = 0; j < noOfAttacks.length; j++) {
                if (successSingleAttack(noOfAttacks[j], kingdom, wallsAttacked, maxStrength)) {
                    successfullAttack++;
                }
            }

            //loop for updating wall strength
            for (int j = 0; j < wallsAttacked.size(); j++) {
                String direction = wallsAttacked.get(j);
                kingdom.updateWallStrength(direction, maxStrength.get(direction));
            }
        }
        return successfullAttack;
    }
}

public class Main {
    public static void main(String[] args) {
        inputClass i = new inputClass();
        i.getInput();
        System.out.println(i.calculateSuccesfulAttack());
    }
}
