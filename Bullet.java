/*
class Bullet.
Used by the ship to shoot enemies.
Used by the enemies to shoot the ship
 */
package gradleproject1;

public class Bullet {

    private final int bullet_x; //x coordinate of bullet
    private int bullet_y; //y coordinate of bullet
    public boolean struck = false; //if the bullet has hit an enemy
    
    //Used to determine if a bullet overlaps with another object
    private int bullet_y_min; 
    private final int bullet_x_min;
    private int bullet_y_max;
    private final int bullet_x_max;

    //Constructor. Creates a bullet starting at a given point
    public Bullet(int x, int y) {

        bullet_x = x;
        bullet_y = y;
        bullet_y_min = bullet_y - 13;
        bullet_x_min = bullet_x - 5;
        bullet_y_max = bullet_y + 13;
        bullet_x_max = bullet_x + 5;
        

    }

    //Advances the bullet upward. Used by the ship
    public void move() {

        bullet_y -= 10;
        bullet_y_max -= 10;
        bullet_y_min -= 10;

    }
    
    //Advances the bullet downward. Used by the enemies
    public void moveDown()
    {
        bullet_y += 5;
        bullet_y_max += 5;
        bullet_y_min += 5;
    }

    //returns bullet x coordinate
    public int getX() {

        return bullet_x;

    }

    //returns the bullet y coordinate
    public int getY() {

        return bullet_y;

    }
    
    /*The following four functions return the maxes
    and mins of the bullet and are used to determine
    if the bullet has hit something.
    */
    public int getXMin()
    {
        return bullet_x_min;
    }
    
    public int getXMax()
    {
        return bullet_x_max;
    }
    
    public int getYMin()
    {
        return bullet_y_min;
    }
    
    public int getYMax()
    {
        return bullet_y_max;
    }
    
}
