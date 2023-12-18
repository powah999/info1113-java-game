package WizardTD;

import processing.core.PImage;

public class Path extends GameObject{
    public static String spritepathstraight = "src/main/resources/WizardTD/path0.png";
    public static String spritepathright = "src/main/resources/WizardTD/path1.png";
    public static String spritepaththreeway = "src/main/resources/WizardTD/path2.png";
    public static String spritepathfourway = "src/main/resources/WizardTD/path3.png";

    public Path(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    public void tick(){
    
    };

    @Override
    public String toString() {
        return "Path";
    }
}