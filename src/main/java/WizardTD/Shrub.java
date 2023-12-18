package WizardTD;

import processing.core.PImage;

public class Shrub extends GameObject{
    public static String spritepath = "src/main/resources/WizardTD/shrub.png";

    public Shrub(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    public void tick(){

    };

    @Override
    public String toString() {
        return "Shrub";
    }
}