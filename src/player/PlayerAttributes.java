package player;

import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;

public class PlayerAttributes {

    public int shooting;
    public int passing;
    public int stamina;
    public int speed;
    public float height;
    public float weight;
    public PreferredFoot preferredFoot;


    public PlayerAttributes() {}

    private PlayerAttributes(int shooting, int passing, int stamina, int speed, float height, float weight, PreferredFoot preferredFoot) {
        this.shooting = shooting;
        this.passing = passing;
        this.stamina = stamina;
        this.speed = speed;
        this.height = height;
        this.weight = weight;
        this.preferredFoot = preferredFoot;
    }

    public  PlayerAttributes generateAttributes(String basePosition) {
        int shooting = 50;
        int passing = 50;
        int stamina = 50;
        int speed = 50;

        String pos = basePosition.toLowerCase();

        if (pos.equals("goalkeeper")) {
            shooting = randomBetween(0, 20);
            passing = randomBetween(10, 30);
            stamina = randomBetween(50, 80);
            speed = randomBetween(40, 70);
        } else if (pos.equals("defender")) {
            shooting = randomBetween(20, 50);
            passing = randomBetween(30, 60);
            stamina = randomBetween(60, 90);
            speed = randomBetween(50, 80);
        } else if (pos.equals("midfielder")) {
            shooting = randomBetween(40, 70);
            passing = randomBetween(50, 90);
            stamina = randomBetween(60, 90);
            speed = randomBetween(60, 90);
        } else if (pos.equals("forward") || pos.equals("striker")) {
            shooting = randomBetween(70, 100);
            passing = randomBetween(40, 70);
            stamina = randomBetween(40, 70);
            speed = randomBetween(70, 100);
        }

        float height = roundToTwoDecimals(randomFloatBetween(1.65f, 2.00f)); // altura em metros
        float weight = roundToTwoDecimals(randomFloatBetween(60.0f, 95.0f)); // peso em kg

        PreferredFoot foot = generatePreferredFoot();

        return new PlayerAttributes(shooting, passing, stamina, speed, height, weight, foot);
    }


    private static float roundToTwoDecimals(float value) {
        return (float)Math.round(value * 100) / 100;
    }


    private static int randomBetween(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    private static float randomFloatBetween(float min, float max) {
        return (float)(Math.random() * (max - min)) + min;
    }

    private static PreferredFoot generatePreferredFoot() {
        double rand = Math.random();
        if (rand < 0.45) return PreferredFoot.Right;
        else if (rand < 0.9) return PreferredFoot.Left;
        else return PreferredFoot.Both;
    }


    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public int getPassing() {
        return passing;
    }

    public int getShooting() {
        return shooting;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStamina() {
        return stamina;
    }

    public PreferredFoot getPreferredFoot() {
        return preferredFoot;
    }


    @Override
    public String toString() {
        String s ="Atributos do jogador: \n";
        s+="Shooting:"+this.shooting;
        s+="\nPassing:"+this.passing;
        s+="\nStamina:"+this.stamina;
        s+="\nSpeed:"+this.speed;
        s+="\nHeight:"+this.height;
        s+="\nWeight:"+this.weight;
        s+="\nPrefered Foot: "+this.preferredFoot.getPreferredFoot();
        return s;
    }
}
