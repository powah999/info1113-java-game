package WizardTD;
import processing.core.PImage;

import processing.core.PApplet;

public abstract class GameObject{
    public int x;
    public int y;
    public PImage sprite;
    public static String spritepath;

    public GameObject(int x, int y, PImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public abstract void tick();

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
