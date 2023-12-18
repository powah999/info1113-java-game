package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;

public abstract class GameObjectSmart{
    public char[][] map;
    public float x;
    public float y;
    public PImage sprite;
    public static String spritepath; 

    public GameObjectSmart(PImage sprite){
        this.sprite = sprite;
    }

    public abstract List<Integer> tick(int frame_time);

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float x){
        this.x = x;
    }

    public void setMap(char[][] map){
        this.map = map;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }
}