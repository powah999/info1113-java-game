package WizardTD;

import processing.core.PImage;

public class Grass extends GameObject{
    public static String spritepath = "src/main/resources/WizardTD/grass.png";

    public Grass(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    public void tick(){

    };

    @Override
    public String toString() {
        return "Grass";
    }
}