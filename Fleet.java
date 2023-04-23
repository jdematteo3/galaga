/*
class Fleet
Creates a string of enemies and has some
functionality beyond a simple list.
 */
package gradleproject1;
import java.util.*;
public class Fleet {

    private ArrayList<Enemy> enemies = new ArrayList<Enemy>(); //list of enemies
    Random rd = new Random();
    private int num;//number of enemies

    //Constructor. Generates a list of enemies with the same path.
    public Fleet(int LoopType, boolean mirror, int numEnemies) {
        for (int i = 0; i < numEnemies; i++) {
            enemies.add(new Enemy("grunt", new Loop(LoopType, mirror)));
        }
        num = numEnemies;
    }

    //returns the number of enemies in the fleet
    public int getNum() {
        return num;
    }

    //returns a specific enemy in the fleet
    public Enemy getEnemy(int enemyNum) {
        return enemies.get(enemyNum);
    }

    //resets a whole fleet back to being alive
    public void reset(int numEnemies)
    {
        enemies.clear();
        num = numEnemies;
        int tempLoopType = (int)(7*Math.random());
        boolean tempMirror = rd.nextBoolean();
        for (int i = 0; i < num; i++) {
            enemies.add(new Enemy("grunt", new Loop(tempLoopType, tempMirror)));
        }
    }
    
    //moves an entire fleet
    public void runFleet() {
        double thresh = 1 / (double) num;
        if(enemies.get(num - 1).getLoop().getT() < 1.0) {
            for (int i = 0; i < num; i++) 
            {
                if (enemies.get(0).getLoop().getT() >= i * thresh) 
                {
                    enemies.get(i).runEnemy();
                }
            }
        }

    }

    //changes the path of a fleet
    public void changeLoop(int LoopType, boolean mirror) {
        for (int i = 0; i < num; i++) {
            enemies.get(i).changeLoop(new Loop(LoopType, mirror));
        }
    }
}
