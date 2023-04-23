/*
class Star
Generates a single star in the background of the game.
 */
package gradleproject1;


public class Star 
{
    private int star_x; //star x coordinate
    private int star_y = 0;//star y coordinate
    private int advanceRate = 0; //determines how fast a star moves
    
    /*Constructor. Generates a random starting location for x at 
    the top of the screen. Then creates a random value to make the star advance
    downward. This random value gives the sensation of flying through space.
    */
    public Star()
    {
        star_x = (int)(750*Math.random()); 
        
        while(advanceRate < 5)
        {
            advanceRate = (int)(10*Math.random());
        }
    }
    
    //returns the x value of the star
    public int getX()
    {
        return star_x;
    }
    
    //returns the y value of the star
    public int getY()
    {
        return star_y;
    }
    
    //moves the star depending on the randomly generated speed
    public void move()
    {
        star_y+=advanceRate;
    }
}
