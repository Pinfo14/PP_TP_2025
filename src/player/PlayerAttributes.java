package player;

import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;

public class PlayerAttributes {

    public int shooting;
    public int passing;
    public int stamina;
    public int speed;
    public float height;
    public float weight;
    public int defence;
    public PreferredFoot preferredFoot;


    public PlayerAttributes() {}

    private PlayerAttributes(int shooting, int passing, int stamina, int speed, float height, float weight, int defence,PreferredFoot preferredFoot) {
        this.shooting = shooting;
        this.passing = passing;
        this.stamina = stamina;
        this.speed = speed;
        this.height = height;
        this.weight = weight;
        this.preferredFoot = preferredFoot;
        this.defence = defence;
    }

    public  PlayerAttributes generateAttributes(String basePosition) {
        int shooting = 50;
        int passing = 50;
        int stamina = 50;
        int speed = 50;
        int defence = 50;

        String pos = basePosition.toLowerCase();

        if (pos.equals("goalkeeper")) {
            defence = randomBetween(60, 100);
            shooting = randomBetween(0, 20);
            passing = randomBetween(10, 30);
            stamina = randomBetween(50, 80);
            speed = randomBetween(40, 70);
        } else if (pos.equals("defender")) {
            defence = randomBetween(30, 90);
            shooting = randomBetween(20, 50);
            passing = randomBetween(30, 60);
            stamina = randomBetween(60, 90);
            speed = randomBetween(50, 80);
        } else if (pos.equals("midfielder")) {
            defence = randomBetween(20, 60);
            shooting = randomBetween(40, 70);
            passing = randomBetween(50, 90);
            stamina = randomBetween(60, 90);
            speed = randomBetween(60, 90);
        } else if (pos.equals("forward") || pos.equals("striker")) {
            defence = randomBetween(10, 30);
            shooting = randomBetween(70, 100);
            passing = randomBetween(40, 70);
            stamina = randomBetween(40, 70);
            speed = randomBetween(70, 100);
        }

        float height = roundToTwoDecimals(randomFloatBetween(1.65f, 2.00f)); // altura em metros
        float weight = roundToTwoDecimals(randomFloatBetween(60.0f, 95.0f)); // peso em kg

        PreferredFoot foot = generatePreferredFoot();

        return new PlayerAttributes(shooting, passing, stamina, speed, height, weight, defence,foot);
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

    public int getDefence() {
        return defence;
    }

    public PreferredFoot getPreferredFoot() {
        return preferredFoot;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Atributos do jogador: ");
        sb.append("Defence:").append(this.defence).append(", ");
        sb.append("Shooting:").append(this.shooting).append(", ");
        sb.append("Passing:").append(this.passing).append(", ");
        sb.append("Stamina:").append(this.stamina).append(", ");
        sb.append("Speed:").append(this.speed).append(", ");
        sb.append("Height:").append(this.height).append(", ");
        sb.append("Weight:").append(this.weight).append(", ");
        sb.append("Preferred Foot:").append(this.preferredFoot);
        return sb.toString();
    }

}
