package WizardTD;

import java.util.List;
import java.util.Map;

public class Config {
    public String layout;
    public List<Wave> waves;
    public int initial_tower_range;
    public double initial_tower_firing_speed;
    public int initial_tower_damage;
    public int initial_mana;
    public int initial_mana_cap;
    public double initial_mana_gained_per_second;
    public int tower_cost;
    public int mana_pool_spell_initial_cost;
    public int mana_pool_spell_cost_increase_per_use;
    public double mana_pool_spell_cap_multiplier;
    public double mana_pool_spell_mana_gained_multiplier;

    public static class Wave {
        public int duration;
        public double pre_wave_pause;
        public List<Monster> monsters;


        // public String type;
        // public int hp;
        // public double speed;
        // public double armour;
        // public int mana_gained_on_kill;
        // public int quantity;
    }

    public static class Monster {
        public String type;
        public int hp;
        public float speed;
        public double armour;
        public int mana_gained_on_kill;
        public int quantity;

    }
}