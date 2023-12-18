package WizardTD;

import processing.core.PImage;

public class WizardHouse extends GameObject{
    public static String spritepath = "src/main/resources/WizardTD/wizard_house.png";

    public WizardHouse(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    public void tick(){

    };

    @Override
    public String toString() {
        return "WizardHouse";
    }
}