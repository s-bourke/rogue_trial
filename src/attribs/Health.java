package attribs;

public class Health {

    private int health;
    private int maxHealth;

    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    void addHealth(int hp){
        health += hp;
        health = Math.min(health, maxHealth);
    }

    void loseHealth(int hp){
        health -= hp;
        health = Math.max(health, 0);
    }
}
