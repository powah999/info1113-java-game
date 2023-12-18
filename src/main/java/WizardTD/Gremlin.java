package WizardTD;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Gremlin extends GameObjectSmart{
    public static String spritepath = "src/main/resources/WizardTD/gremlin.png";
    public String death1 = "src/main/resources/WizardTD/gremlin1.png";
    public String death2 = "src/main/resources/WizardTD/gremlin2.png";
    public String death3 = "src/main/resources/WizardTD/gremlin3.png";
    public String death4 = "src/main/resources/WizardTD/gremlin4.png";
    public String death5 = "src/main/resources/WizardTD/gremlin5.png";

    public int[] wizardcoords;
    public char[][] map;
    public List<List<List<int[]>>> allPaths;
    PathFinder pathFinder;
    List<int[]> path;
    int count = 0;
    
    String edge;

    public float speed;
    public String type;
    public int hp;
    public int maxhp;
    public double armour;
    public int mana_gained_on_kill;

    public int spawn_time;

    public int death_sprite = 1;
    public int last_frametime = -1;
    public boolean dont_draw = false; 

    
    
    @Override
    public void setMap(char[][] map){
        this.map = map;
        PathFinder pathFinder = new PathFinder(map);
        this.pathFinder = pathFinder;
        this.allPaths = pathFinder.shortestpaths;
    }

    public void setRandPath(){
        try{
            Random random = new Random();
            int spawnIndex = random.nextInt(this.allPaths.size());
            List<List<int[]>> which_spawn = this.allPaths.get(spawnIndex);

            int pathIndex = random.nextInt(which_spawn.size());
            this.path = which_spawn.get(pathIndex);


            if(path.get(0)[0] == 0){
                edge = "up";
                this.x = path.get(0)[1]*32+6;
                this.y = path.get(0)[0]*32+46 - 32;

            } else if(path.get(0)[0] == 19){
                edge = "down";
                this.x = path.get(0)[1]*32+6;
                this.y = path.get(0)[0]*32+46 + 32;
                
            } else if(path.get(0)[1] == 0){
                edge = "left";
                this.x = path.get(0)[1]*32+6 - 32;
                this.y = path.get(0)[0]*32+46;

            } else if(path.get(0)[1] == 19){
                edge = "right";
                this.x = path.get(0)[1]*32+6 + 32;
                this.y = path.get(0)[0]*32+46;
    
            }

        }catch (Exception e){
            System.out.println("NO MAP");
        }
    }



    public Gremlin(PImage sprite, int[] wizardcoords){
        super(sprite);
        this.wizardcoords = wizardcoords;
    }



    public List<Integer> tick(int frame_time){
        if(hp <= 0){
            if(death_sprite < 6){
                if(last_frametime == -1){
                    last_frametime = frame_time;
                    List<Integer> gremlin_info = new ArrayList<>();
                    gremlin_info.add(0);
                    gremlin_info.add(death_sprite);
                    return gremlin_info;

                } else{
                    if((frame_time-last_frametime) % 4 == 0){
                        this.death_sprite++;
                    }
                    if(death_sprite<6){
                        List<Integer> gremlin_info = new ArrayList<>();
                        gremlin_info.add(0);
                        gremlin_info.add(this.death_sprite);
                        return gremlin_info;

                    }
                    this.dont_draw = true;
                    List<Integer> gremlin_info = new ArrayList<>();
                    gremlin_info.add(0);
                    gremlin_info.add(6);
                    return gremlin_info;
                }


            } else{
                this.dont_draw = true;
                return null;

            }

        } else{

            if(frame_time < spawn_time){
            return null;

            }else{

                if(this.count<this.path.size()){   
                    float next_x = this.path.get(count)[1]*32 + 6;
                    float next_y = this.path.get(count)[0]*32+40 + 6;
                    
                    if(next_x > this.x && next_y == this.y){
                        float huhx = this.x;
                        float tempx = huhx + this.speed;
                        if(tempx >= next_x){
                            this.x = next_x;
                            this.count++;
                        } else{
                            this.x += this.speed;
                        }

                    } else if(next_x < this.x && next_y == this.y){
                        float huhx = this.x;
                        float tempx = huhx - this.speed;
                        if(tempx <= next_x){
                            this.x = next_x;
                            this.count++;
                        } else{
                            this.x -= this.speed;
                        }

                    } else if(next_x == this.x && next_y > this.y){
                        float huhy = this.y;
                        float tempy = huhy + this.speed;
                        if(tempy >= next_y){
                            this.y = next_y;
                            this.count++;
                        } else{
                            this.y += this.speed;
                        }
                        
                    } else if(next_x == this.x && next_y < this.y){
                        float huhy = this.y;
                        float tempy = huhy- this.speed;
                        if(tempy <= next_y){
                            this.y = next_y;
                            this.count++;
                        } else{
                            this.y -= this.speed;
                        }
                    }

                    
                    if(this.count == this.path.size()){
                        count = 0;
                        this.setRandPath();
                        List<Integer> gremlin_info = new ArrayList<>();
                        gremlin_info.add(1);
                        gremlin_info.add(hp); 
                        return gremlin_info;
                    }
                }
                return null;
            }

        }
        

    }

    @Override
    public void draw(PApplet app) {
        if(!this.dont_draw){
            app.image(this.sprite, this.x, this.y);
            if(this.hp > 0){
                app.fill(255, 0, 0);
                app.rect(this.x, this.y - 6, 22, 3);

                app.fill(0, 255, 0);
                float percentage = (float) hp / (float) maxhp;
                app.rect(this.x, this.y - 6, 22*percentage, 3);

            }

        }
    }

    public void setSpeed(float speed){
        this.speed = speed;

    }

    public void setHp(int hp) {
        this.hp = hp;
        this.maxhp = hp;
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

    public void setSpawntime(int spawntime){
        this.spawn_time = spawntime;
    }


    
    


    @Override
    public String toString() {
        return "Gremlin";
    }

}
