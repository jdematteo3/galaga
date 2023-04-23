/*
class Enemy
Generates an enemy to be used in the game.
Organized into fleets in the class Fleet. 
 */
package gradleproject1;


public class Enemy {

    private String type; //type of enemy
    private Loop path; //path of enemy
    public boolean alive = true; //enemies are elive
    public boolean hitShip = false; //if the enemy has hit the ship
    private double shootPoint; //randomly determined point on the loop where the enemy shoots
    public double deathParameter = 0; //Utilized when an enemy dies in creating the explosion
    public int deathSpotX;//Utilized when an enemy dies in creating the explosion
    public int deathSpotY;//Utilized when an enemy dies in creating the explosion
    

    //Constructor. Gives a random shoot point to the enemy. 
    public Enemy(String enemyType, Loop loop) {
        path = loop;
        type = enemyType;
        generateShootPoints();
    }

    //changes an enemy's path
    public void changeLoop(Loop loop) {
        path = loop;
        path.resetT();
        hitShip = false;
    }

    //gets the loop of an enemy
    public Loop getLoop() {
        return path;
    }

    //Runs the enemy along its path
    public void runEnemy() {
        path.runLoop();
    }
    
    //returns the point when the enemy shoots
    public double getShootPoint(){
        
        return shootPoint;
    }
    
    /*
    Creates the shoot points for an enemy.
    After experimenting, the enemy only shoots once along its path.
    More than one shot per path is too difficult.
    */
    private void generateShootPoints() {

        shootPoint = ((double)(int)(250*Math.random()))/250;
        
    }
    
    //The following functions are utilized when determining if an enemy is dead or not
    public int getYMax()
    {
        return path.getY() + 20;
    }
    public int getYMin()
    {
        return path.getY() - 20;
    }
    public int getXMax()
    {
        return path.getX() + 20;
    }
    public int getXMin()
    {
        return path.getX() - 20;
    }
    
}
