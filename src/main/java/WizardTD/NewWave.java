package WizardTD;


public class NewWave {
    
    int num;
    int dur;
    double prepause;

    float speed;
    int hp;
    String type;
    double armour;
    int mana_gained_on_kill;
    int quantity;

    public NewWave(){
    }

    public int getNum() {
        return num;
    }

    public double getPrepause() {
        return prepause;
    }

    public int getDur() {
        return dur;
    }

    public float getSpeed() {
        return speed;
    }

    public void setNum(int num) {
        this.num = num;
    } 
    
    public void setDur(int dur) {
        this.dur = dur;

    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPrepause(double prepause) {
        this.prepause = prepause;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setType(String type) {
        this.type= type;
    }

    public void setArmour(double armour) {
        this.armour = armour;
    }

     public void setMana(int mana) {
        this.mana_gained_on_kill = mana;
    }

}