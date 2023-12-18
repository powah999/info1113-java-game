package WizardTD;


import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import javax.sound.sampled.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;


import WizardTD.Config.Wave;




public class App extends PApplet {


    // private PImage sprite;

    private String gremlin_path = "src/main/resources/WizardTD/gremlin.png";

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public String configPath;

    public Random random = new Random();

    public String layout;
    public char[][] map;
    public GameObject[] tiles = new GameObject[401];
    GameObject saveWizardHouse;
    int[] wizardcoords;

    List<List<Gremlin>> wave_gremlins = new ArrayList<>();
    public List<NewWave> waves = new ArrayList<>();

    public List<Integer> starttimestamps = new ArrayList<>();
    public List<Integer> endtimestamps = new ArrayList<>();
    public List<Integer> list_time_till = new ArrayList<>();

    int increment_list_time_till = 0;
    int time_till_next_wave;

    int next_wave_number = 1;

    int ogmana;
    int mana;
    int mana_cap;
    double mana_generation;


    int frame_time = 0;
    int seconds = 0;
    int highest_wave;
    boolean pause = false;
    boolean gameover = false;
    boolean win = false;

    int og_num_gremlins;
    int current_num_gremlins;

    SoundPlayer soundplayer;
    Thread audioThread;


	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        



    }   

    public char[][] MapToTile(String filename){
        char[][] board = new char[20][20];

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int num_lines = 0;
           
            for (int i = 0; i < 20 && scanner.hasNextLine(); i++) {
                String line = scanner.nextLine();
                num_lines++;

                // Ensure the line has exactly 20 characters
                for (int j = 0; j < line.length() && j < 20; j++) {
                    board[i][j] = line.charAt(j);
                }
                if (line.length() < 20){
                    for(int z = line.length() - 1; z < 20; z++){
                        board[i][z] = ' ';
                    }
                }

            }
            if(num_lines<20){
                for(int i = num_lines - 1; i < 20; i++){
                    for(int j = 0; j < 20; j++){
                        board[i][j] = ' ';
                    }
                }
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        return board;
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        

        this.configPath = "config.json";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(new File(configPath), Config.class);

            layout = config.layout;
            for(Wave i: config.waves){

                int hp = i.monsters.get(0).hp;
                String type = i.monsters.get(0).type;
                float speed = i.monsters.get(0).speed;
                double armour = i.monsters.get(0).armour;
                int mana_gained_on_kill = i.monsters.get(0).mana_gained_on_kill;
                int quantity = i.monsters.get(0).quantity;

                NewWave addWave = new NewWave();
                addWave.setDur(i.duration);
                addWave.setPrepause(i.pre_wave_pause);
                addWave.setSpeed(speed);
                addWave.setNum(quantity);
                addWave.setHp(hp);
                addWave.setType(type);
                addWave.setArmour(armour);
                addWave.setMana(mana_gained_on_kill);
                this.waves.add(addWave);
            }

            ogmana = config.initial_mana;
            mana = ogmana;
            mana_cap = config.initial_mana_cap;
            System.out.println(mana_cap);
            System.out.println(mana);
            mana_generation = config.initial_mana_gained_per_second;
            

        } catch (IOException e) {
            e.printStackTrace();
        }

        map = MapToTile(layout);

        int num = 0;
        int y_map = 0;
        int y_wizard = -1;
        int x_wizard = -1;
        int y_wizard_map = -1;
        int x_wizard_map = -1;
        for(int y = 40; y <= 648; y += 32){
            int x_map = 0;
            for(int x = 0; x <= 608; x += 32){
                if(map[y_map][x_map] == ' '){

                    this.tiles[num] = new Grass(x, y, this.loadImage(Grass.spritepath));
                } else if(map[y_map][x_map] == 'S'){

                    this.tiles[num] = new Shrub(x, y, this.loadImage(Shrub.spritepath));
                } else if(map[y_map][x_map] == 'X'){
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map][x_map-1] == 'X' && map[y_map+1][x_map] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathfourway));
                            num++;
                            x_map++;
                            continue;
                        }
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map][x_map-1] == 'X' && map[y_map+1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepaththreeway));
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map][x_map-1] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepaththreeway));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 180);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map+1][x_map] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepaththreeway));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 270);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map-1] == 'X' && map[y_map+1][x_map] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepaththreeway));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 90);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map-1] == 'X' && map[y_map+1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathright));
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map+1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathright));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 270);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathright));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 180);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map-1] == 'X' && map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathright));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 90);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map-1] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathstraight));
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map][x_map+1] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathstraight));
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map+1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathstraight));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 90);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
                    try {
                        if(map[y_map-1][x_map] == 'X'){
                            this.tiles[num] = new Path(x, y, this.loadImage(Path.spritepathstraight));
                            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 90);
                            num++;
                            x_map++;
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // Code to handle the exception
                    }
    


                } else if(map[y_map][x_map] == 'W'){
                    y_wizard = y;
                    x_wizard = x;
                    y_wizard_map = y_map;
                    x_wizard_map = x_map;
                    this.tiles[num] = new Grass(x, y, this.loadImage(Grass.spritepath));
                }
                num++;
                x_map++;
            }
            y_map++;
        }


        wizardcoords = new int[] {x_wizard, y_wizard}; 
        char up = map[y_wizard_map-1][x_wizard_map];
        char down = map[y_wizard_map+1][x_wizard_map];
        char left = map[y_wizard_map][x_wizard_map-1];
        char right = map[y_wizard_map][x_wizard_map+1];


        if(up == 'X'){
            this.tiles[num] = new WizardHouse(x_wizard-8, y_wizard-8, this.loadImage(WizardHouse.spritepath));
            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 270);
        } else if(down == 'X'){
            this.tiles[num] = new WizardHouse(x_wizard-8, y_wizard-8, this.loadImage(WizardHouse.spritepath));
            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 90);            
        } else if(left == 'X'){
            this.tiles[num] = new WizardHouse(x_wizard-8, y_wizard-8, this.loadImage(WizardHouse.spritepath));
            this.tiles[num].sprite = rotateImageByDegrees(this.tiles[num].sprite, 180);
        } else if(right == 'X'){
            this.tiles[num] = new WizardHouse(x_wizard-8, y_wizard-8, this.loadImage(WizardHouse.spritepath));
        }
    
        int count1 = 0;
        for(int x = 0; x < waves.size();x++){
            int temppause = (int) waves.get(x).prepause*60;
            int tempdur = (int) waves.get(x).dur*60;

            count1 += temppause;
            starttimestamps.add(count1);
            count1 += tempdur;
            endtimestamps.add(count1);

        }

        this.wave_gremlins = initialize_gremlins();
        for(List<Gremlin> i:wave_gremlins){
            for(Gremlin j: i){
                og_num_gremlins += 1;
            }
        }
        current_num_gremlins = og_num_gremlins;
        

        for(int x = 0; x < starttimestamps.size();x++){
            if(x == 0){
                list_time_till.add((int) starttimestamps.get(x)/60);
            } else{
                int next = (int) starttimestamps.get(x)/60;
                int prev = (int) starttimestamps.get(x-1)/60;
                int difference = next - prev;
                list_time_till.add(difference);
            }
        }
        time_till_next_wave = list_time_till.get(0);


        this.soundplayer = new SoundPlayer("src/main/resources/WizardTD/bg.wav", true);
        soundplayer.play();

    }

    public List<List<Gremlin>> initialize_gremlins(){
        int count2 = 0;
        List<List<Gremlin>> temp_wave_gremlins = new ArrayList<>();
        for(NewWave i: this.waves){
            List<Gremlin> gremlins = new ArrayList<>();

            int delay = i.dur*60/i.num;
            int start_time = starttimestamps.get(count2);
            int multiplier = 0;

            for(int x = 0; x < i.num; x++){
                Gremlin tempgremlin = new Gremlin(this.loadImage(gremlin_path), wizardcoords);
                tempgremlin.setMap(map);
                tempgremlin.setRandPath();

                tempgremlin.setArmour(i.armour);
                tempgremlin.setSpeed(i.speed);
                tempgremlin.setHp(i.hp);
                tempgremlin.setMana(i.mana_gained_on_kill);
                tempgremlin.setType(i.type);

                int temp_spawn_time = start_time + delay*multiplier;
                tempgremlin.setSpawntime(temp_spawn_time);

                gremlins.add(tempgremlin);

                multiplier++;
            }
            temp_wave_gremlins.add(gremlins);
            count2++;

        }
        return temp_wave_gremlins;

    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        if ((key == 'P' || key == 'p')) {
            pause = !pause;

          }
        if ((key == 'r' || key == 'R')) {
            seconds = 0;
            frame_time = 0;
            next_wave_number = 1;
            time_till_next_wave = list_time_till.get(0);
            mana = ogmana;
            gameover = false;
            pause = false;
            current_num_gremlins = og_num_gremlins;
            win = false;
            this.wave_gremlins = initialize_gremlins();






          }
        if(key== 'k' || key == 'K'){
            Random random = new Random();
            int randomListIndex = random.nextInt(wave_gremlins.size());
            int randomElementIndex = random.nextInt(wave_gremlins.get(randomListIndex).size());
            wave_gremlins.get(randomListIndex).get(randomElementIndex).hp -= 1;
        }

        
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /*@Override
    public void mouseDragged(MouseEvent e) {

    }*/

    /**
     * Draw all elements in the game by current frame.
     */



    public void updateClock(){
        if(frame_time % 60 == 0){
            seconds++;
            time_till_next_wave -= 1;
            mana += mana_generation;
            System.out.println(seconds);
        }
    }

    public void resetClock(){
        this.frame_time = 0;
        this.seconds = 0;
    }

    public void drawManaBar(int currentmana, int mana_cap) {

        float percentage = (float) currentmana/ (float) mana_cap;

        fill(0);
        textSize(16);
        textAlign(LEFT);
        text("MANA:", 370, 26);
        
        fill(255);
        stroke(0);
        rect(427, 8, 306, 24);
        
        fill(173, 216, 230);
        noStroke();
        rect(430, 10, 424 * percentage, 20);

        fill(0);
        textSize(16);
        text(currentmana + " / " + mana_cap, 535, 25);
    } 

	@Override
    public void draw() {


        textAlign(LEFT);
        if(pause == false && !gameover && !win){
            frame_time++;
            updateClock();
        }
        
        



    
        background(255,0,0);
        for(GameObject i: this.tiles){
            if(i.toString() == "WizardHouse"){
                saveWizardHouse = i;
            } else{
                i.draw(this);
            }
        }

        // this.gremlin.draw(this);   
        // this.gremlin.tick();

        if(!gameover && !win){
            for(List<Gremlin> i: wave_gremlins){
                for(Gremlin gremlin:i){
                    gremlin.draw(this);
                    List<Integer> a = null;
                    if(!pause){
                        a = gremlin.tick(this.frame_time);
                    }
                    if(a != null){
                        if(a.get(0) == 1){
                        float temphp = a.get(1);
                        if(mana - temphp <= 0){
                            mana = 0;
                            gameover = true;
                            } else{
                                mana -= temphp;
                            }
                        } else{
                            int sprite_status = a.get(1);
                            PImage tempsprite = null;
                            
                            if(sprite_status == 1){
                                tempsprite = loadImage(gremlin.death1);
                            } else if(sprite_status == 2){
                                tempsprite = loadImage(gremlin.death2);
                            } else if(sprite_status == 3){
                                tempsprite = loadImage(gremlin.death3);                               
                            } else if(sprite_status == 4){
                                tempsprite = loadImage(gremlin.death4);                                
                            } else if(sprite_status == 5){
                                tempsprite = loadImage(gremlin.death5);                                
                            } else if(sprite_status == 6){
                                this.mana += gremlin.mana_gained_on_kill;
                            }

                            if(tempsprite != null){
                                gremlin.setSprite(tempsprite);
                            }

                        }

                    }
                }
            }


        }


        int count_deaths = 0;
        for(List<Gremlin> list:wave_gremlins){
            for(Gremlin gremlin: list){
                if(gremlin.dont_draw){
                    count_deaths++;
                }
            }
        }
        if(count_deaths == og_num_gremlins){
            win = true;
        }

    


        fill(132, 115, 74);
        noStroke();
        rect(0, 0, 760, 40);
        rect(640, 40, 120, 640);
        fill(0);
        textSize(24);

        if(!gameover){
            // text(String.format("Mana: %s", mana), 640, 30);
            drawManaBar(mana, mana_cap);

        }

        if(!gameover || !win){
            if (next_wave_number <= starttimestamps.size()){
                String formattedString = String.format("Wave %s starts: %s", next_wave_number, time_till_next_wave);
                fill(0);
                textSize(24);
                text(formattedString, 20, 30);
                if(frame_time >= starttimestamps.get(next_wave_number-1)){
                next_wave_number++;
                if(next_wave_number <= starttimestamps.size()){
                    time_till_next_wave = list_time_till.get(next_wave_number-1);
                }
                }
            }
        }


        this.saveWizardHouse.draw(this);
        if(gameover){
            fill(0);
            textSize(64);
            textAlign(CENTER, CENTER);
            text("YOU LOST\npress 'r' to restart", 320, 40+320);

        }

        if(win){
            fill(0);
            textSize(64);
            textAlign(CENTER, CENTER);
            text("YOU WON\npress 'r' to restart", 320, 40+320);

        }
    }


    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
