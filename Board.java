package gradleproject1;

/*
class Board
The following class is the main class of the program. 
It creates the board and contains all functionality of the game.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 750; //Board width
    private final int B_HEIGHT = 750; //Board height
    private final int DELAY = 10; //Reframe rate

    Random rd = new Random();
    
    private int ship_x; //ship x-coordinate
    private int ship_y; //ship y-coordinate
    
    //These set boundaries for the ship to move
    private int ship_x_max; 
    private int ship_x_min;
    private int ship_y_max;
    private int ship_y_min;
    
    private int level = 1; //level
    
    private double shipDeathParam = 0; //Variable that is utilized when the ship is hit
    
    /*These offset the images since some of them are 
    slightly off center. They were experimentally determined. 
    */
    private final int ship_x_offset = -20;
    private final int ship_y_offset = 0;
    private final int enemy_x_offset = -25;
    private final int enemy_y_offset = 0;
    private final int bullet_x_offset = -143;
    private final int bullet_y_offset = -215;

    //variables that indicate what screen to play
    private boolean inGame = false;
    private boolean changeLevel = false;
    private boolean pregame = true;
    private boolean shipHit = false;
    private boolean gameOver = false;

    //Included so that the refresh rate is functional
    private Timer timer;
    
    //Images that are included
    private Image ship;
    private Image bulletImage;
    private Image gruntImage;

    private int lives = 3; //lives
    private int score = 0; //score
    private int highScore = 1000; //High score --> Changes when score becomes higher

    //Creates lists for the bullets, enemies and stars
    private ArrayList<Bullet> shipBullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
    private ArrayList<Star> stars = new ArrayList<Star>();
    private ArrayList<Fleet> fleets = new ArrayList<Fleet>();
    
    //indicate direction of the ship
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false; 
    

    //Constructor
    public Board() {

        initBoard();

    }
    
    //Called in the constructor. Sets up the game
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        fleets.add(new Fleet((int)(7*Math.random()), rd.nextBoolean(), 1)); //Creates the first enemy
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //loads images from the computer
    private void loadImages() {

        ImageIcon bulletIcon = new ImageIcon("/Volumes/GoogleDrive/My Drive/AP Computer Science Principles/BulletImage.png");
        bulletImage = bulletIcon.getImage();

        ImageIcon shipIcon = new ImageIcon("/Volumes/GoogleDrive/My Drive/AP Computer Science Principles/ship.png");
        ship = shipIcon.getImage();
        
        ImageIcon gruntIcon = new ImageIcon("/Volumes/GoogleDrive/My Drive/AP Computer Science Principles/GruntImage.png");
        gruntImage = gruntIcon.getImage();
        

    }

    //Sets initial locations of the ship 
    private void initGame() {

        ship_x = 375;
        ship_y = 600;
        ship_x_max = ship_x + 20;
        ship_x_min = ship_x - 20;
        ship_y_max = ship_y + 16;
        ship_y_min = ship_y - 16;

    }

    //Periodically called to display the picture
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    //Draws all necessary components
    private void doDrawing(Graphics g) {
        drawStars(g);
        drawShipBullets(g);
        for(int i = 0; i < fleets.size(); i++)
        {
            for(int j = 0; j < fleets.get(i).getNum(); j++)
            {
                if(!fleets.get(i).getEnemy(j).alive && fleets.get(i).getEnemy(j).deathParameter < 1)
                {
                    drawDeathSequence(g, fleets.get(i).getEnemy(j).deathParameter, fleets.get(i).getEnemy(j).deathSpotX, fleets.get(i).getEnemy(j).deathSpotY);
                    fleets.get(i).getEnemy(j).deathParameter+=.02;
                }
            }
        }
        if(inGame)
        {
            g.drawImage(ship, ship_x + ship_x_offset, ship_y + ship_y_offset, this);
            drawFleets(g);
            drawEnemyBullets(g);
        }
        if(changeLevel)
        {
            g.drawImage(ship, ship_x + ship_x_offset, ship_y + ship_y_offset, this);
            String msg = "Level " + level + " complete! Press enter to move on.";
            Font font = new Font("TimesRoman", Font.BOLD, 30);
            FontMetrics metr = getFontMetrics(font);

            g.setColor(Color.white);
            g.setFont(font);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        }
        if(pregame)
        {
            ship_x = 375;
            ship_y = 600;
            g.drawImage(ship, ship_x + ship_x_offset, ship_y + ship_y_offset, this);
            Font font = new Font("TimesRoman", Font.BOLD, 100);
            FontMetrics metr = getFontMetrics(font);
            g.setColor(Color.red);
            g.setFont(font);
            g.drawString("GALAGA", (B_WIDTH - metr.stringWidth("GALAGA")) / 2, 200);
            
            Font hscore = new Font("TimesRoman", Font.BOLD, 50);
            FontMetrics subMetr = getFontMetrics(hscore);
            g.setColor(Color.blue);
            g.setFont(hscore);
            g.drawString("HIGH SCORE: " + highScore, (B_WIDTH - subMetr.stringWidth("HIGH SCORE: " + highScore)) / 2, 300);
            
            Font directions = new Font("TimesRoman", Font.BOLD, 25);
            FontMetrics dirMetr = getFontMetrics(directions);
            g.setFont(directions);
            g.drawString("→ : Move Right", (B_WIDTH - dirMetr.stringWidth("→ : Move Right")) / 2, 350);
            g.drawString("← : Move Left", (B_WIDTH - dirMetr.stringWidth("← : Move Left")) / 2, 375);
            g.drawString("↑ : Move Up", (B_WIDTH - dirMetr.stringWidth("↑ : Move Up")) / 2, 400);
            g.drawString("↓ : Move Down", (B_WIDTH - dirMetr.stringWidth("↓ : Move Down")) / 2, 425);
            g.drawString("SPACE : Shoot", (B_WIDTH - dirMetr.stringWidth("SPACE : Shoot")) / 2, 450);
            g.drawString("Press ENTER to begin.", (B_WIDTH - dirMetr.stringWidth("Press ENTER to begin.")) / 2, 550);
            
            
        }
        if(shipHit)
        {
            drawFleets(g);
            if(shipDeathParam < 1)
            {
                drawDeathSequence(g, shipDeathParam, ship_x, ship_y);
                shipDeathParam+=.02;
            }
            
            Font death = new Font("TimesRoman", Font.BOLD, 100);
            FontMetrics deathMetr = getFontMetrics(death);
            g.setColor(Color.red);
            g.setFont(death);
            g.drawString("YOU GOT HIT", (B_WIDTH - deathMetr.stringWidth("YOU GOT HIT")) / 2, 200);
            
            String msg;
            if(lives == 1)
            {
                msg = lives + " life left";
            }
            else
            {
                msg = lives + " lives left";
            }
            Font livesLeft = new Font("TimesRoman", Font.BOLD, 50);
            FontMetrics llMetr = getFontMetrics(livesLeft);
            g.setFont(livesLeft);
            g.drawString(msg, (B_WIDTH - llMetr.stringWidth(msg)) / 2, 300);
            
            Font cont = new Font("TimesRoman", Font.BOLD, 25);
            FontMetrics contMetr = getFontMetrics(cont);
            g.setColor(Color.white);
            g.setFont(cont);
            g.drawString("Press ENTER to continue.", (B_WIDTH - contMetr.stringWidth("Press ENTER to continue.")) / 2, 600);
        }
        if(gameOver)
        {
            drawFleets(g);
            if(shipDeathParam < 1)
            {
                drawDeathSequence(g, shipDeathParam, ship_x, ship_y);
                shipDeathParam+=.02;
            }
            Font gameOver = new Font("TimesRoman", Font.BOLD, 100);
            FontMetrics goMetr = getFontMetrics(gameOver);
            g.setColor(Color.red);
            g.setFont(gameOver);
            g.drawString("GAME OVER", (B_WIDTH - goMetr.stringWidth("GAME OVER")) / 2, 200);
            
            Font cont = new Font("TimesRoman", Font.BOLD, 25);
            FontMetrics contMetr = getFontMetrics(cont);
            g.setColor(Color.white);
            g.setFont(cont);
            g.drawString("Press ENTER to return to the home screen.", (B_WIDTH - contMetr.stringWidth("Press ENTER to return to the home screen.")) / 2, 600);
        }
        drawStatusBar(g);
        

    }
    
    private void drawStatusBar(Graphics g)
    {
        g.setColor(Color.yellow);
        g.fillRect(0, 700, 750, 50);
        g.setColor(Color.red);
        Font titleFont = new Font("TimesRoman", Font.BOLD, 30);
        FontMetrics titleFontMetr = getFontMetrics(titleFont);
        g.setFont(titleFont);
        g.drawString("GALAGA", (B_WIDTH - titleFontMetr.stringWidth("GALAGA")) / 2, 735);
        Font subtitleFont = new Font("Serif Bold", Font.BOLD, 12);
        g.setFont(subtitleFont);
        g.setColor(Color.blue);
        g.drawString("LIVES:", 30, 730);
        for(int i = 0; i < lives; i++)
        {
            g.drawImage(ship, 80 + (50*i), 703, this);
        }
        if(score > highScore)
            highScore = score;
        g.drawString("SCORE:  " + score + "  HIGH SCORE:  " + highScore, 500, 730);
        
        
        
    }
    
    //Draws the explosion of an enemy or the ship
    private void drawDeathSequence(Graphics g, double deathParam, int x_center, int y_center)
    {
        g.setColor(Color.yellow);
        for(int i = 0; i < 5; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                double[] vec = generateRandomUnitVector();
                g.fillRect((int)(x_center + i*20*deathParam*vec[0]), (int)(y_center + i*20*deathParam*vec[1]), 1, 1);
            }
        }
    }
    
    //utilized in the drawDeathSequence function
    private double[] generateRandomUnitVector()
    {
        double y = (5*Math.random())-2.5;
        double x = (5*Math.random())-2.5;
        while(x == 0 && y == 0)
        {
            y = (5*Math.random())-2.5;
            x = (5*Math.random())-2.5;
        }
        double[] temp = {x/Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)), y/Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2))};
        return temp;
    }
    
    //Abstraction that draws the stars on the screen
    private void drawStars(Graphics g)
    {
        g.setColor(Color.white);
        for(int i = 0; i < stars.size(); i++)
        {
            g.fillRect(stars.get(i).getX(), stars.get(i).getY(), 2, 2);
        }
    }
    
    //Draws the bullets for the enemies
    private void drawEnemyBullets(Graphics g)
    {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if(!enemyBullets.get(i).struck)
                g.drawImage(bulletImage, enemyBullets.get(i).getX() + bullet_x_offset, enemyBullets.get(i).getY() + bullet_y_offset, this);
        }
    }

    //Draws the bullets for the ship
    private void drawShipBullets(Graphics g) {
        for (int i = 0; i < shipBullets.size(); i++) {
            if(!shipBullets.get(i).struck)
                g.drawImage(bulletImage, shipBullets.get(i).getX() + bullet_x_offset, shipBullets.get(i).getY()+bullet_y_offset, this);
        }
    }

    //Draws all enemies
    private void drawFleets(Graphics g) {
        for(int i = 0; i < fleets.size(); i++)
        {
            for (int j = 0; j < fleets.get(i).getNum(); j++) {
                if(fleets.get(i).getEnemy(j).alive)
                    g.drawImage(gruntImage, fleets.get(i).getEnemy(j).getLoop().getX() + enemy_x_offset, fleets.get(i).getEnemy(j).getLoop().getY() + enemy_y_offset, this);
            }
        }
    }

    //Shoots a bullet
    private void shootBullet() {

        shipBullets.add(new Bullet(ship_x, ship_y));
    }

    //Shoots enemy bullet for all enemies
    private void shootEnemyBullet()
    {
        for(int i = 0; i < fleets.size(); i++)
        {
            for(int j = 0; j < fleets.get(i).getNum(); j++)
            {
                if((fleets.get(i).getEnemy(j).getLoop().getT() < fleets.get(i).getEnemy(j).getShootPoint() + .001) && (fleets.get(i).getEnemy(j).getLoop().getT() > fleets.get(i).getEnemy(j).getShootPoint() - .001) && (fleets.get(i).getEnemy(j).alive))
                {
                    enemyBullets.add(new Bullet(fleets.get(i).getEnemy(j).getLoop().getX(), fleets.get(i).getEnemy(j).getLoop().getY()));
                }
            }
        }
    }
    
    //Advances the bullets of the enemy
    private void moveEnemyBullets() {
        for(int i = 0; i < enemyBullets.size(); i++)
        {
                enemyBullets.get(i).moveDown();
        }
    }
    
    //advances the bullets of the ship
    private void moveShipBullets() {
        for (int i = 0; i < shipBullets.size(); i++) {
            shipBullets.get(i).move();
            if(shipBullets.get(i).getY() < -50)
                shipBullets.remove(i);
        }
    }

    //Called to move the ship around within the boundaries of the board
    private void moveShip() {
        if (up == true && ship_y > 0) {
            ship_y -= 5;
        }
        if (down == true && ship_y < 640) {
            ship_y += 5;
        }
        if (right == true && ship_x < 725) {
            ship_x += 5;
        }
        if (left == true && ship_x > 25) {
            ship_x -= 5;
        }
        
        ship_x_max = ship_x + 20;
        ship_x_min = ship_x - 20;
        ship_y_max = ship_y + 16;
        ship_y_min = ship_y - 16;
    }

    //Advances all stars down a random amount
    public void handleStars()
    {
        stars.add(new Star());

        
        for(int i = 0; i < stars.size(); i++)
        {
            stars.get(i).move();
            if(stars.get(i).getY() > 760)
                stars.remove(i);
        }
        
        
    }
    //Main function periodically called
    @Override
    public void actionPerformed(ActionEvent e) {

        //Actions always occuring
        moveShipBullets();
        handleStars();
        
        //Actions while playing
        if (inGame) {
            moveShip();
            for(int i = 0; i < fleets.size(); i++)
            {
                fleets.get(i).runFleet();
            }
            shootEnemyBullet();
            moveEnemyBullets();
            checkEnemyDeaths();
            checkShipHit();
            for(int i = 0; i < fleets.size(); i++)
            {
                if(fleets.get(i).getEnemy(fleets.get(i).getNum()-1).getLoop().getT() >= 1)
                {
                    fleets.get(i).changeLoop((int)(7*Math.random()), rd.nextBoolean());
                }
            }
            

        }
        
        //Actions in the homescreen
        if(pregame)
        {
            for(int i = 0; i < fleets.size(); i++)
            {
                fleets.get(i).runFleet();
            }
        }
        
        //Actions when the ship gets hit
        if(shipHit)
        {
            moveEnemyBullets();
            checkEnemyDeaths();
            for(int i = 0; i < fleets.size(); i++)
            {
                if(fleets.get(i).getEnemy(fleets.get(i).getNum()-1).getLoop().getT() >= 1)
                {
                    fleets.get(i).changeLoop((int)(7*Math.random()), rd.nextBoolean());
                }
            }
            for(int i = 0; i < fleets.size(); i++)
            {
                fleets.get(i).runFleet();
            }
        }
        
        //Actions when the game is over
        if(gameOver)
        {
            moveEnemyBullets();
            checkEnemyDeaths();
            for(int i = 0; i < fleets.size(); i++)
            {
                if(fleets.get(i).getEnemy(fleets.get(i).getNum()-1).getLoop().getT() >= 1)
                {
                    fleets.get(i).changeLoop((int)(7*Math.random()), rd.nextBoolean());
                }
            }
            for(int i = 0; i < fleets.size(); i++)
            {
                fleets.get(i).runFleet();
            }
        }

        repaint();
    }
    
    //Determines which enemies have been struck by a ship bullet
    public void checkEnemyDeaths()
    {

        for(int i = 0; i < shipBullets.size(); i++)
        {
            for(int j = 0; j < fleets.size(); j++)
            {
                for(int k = 0; k < fleets.get(j).getNum(); k++)
                {
                    if(bulletContactEnemy(shipBullets.get(i), fleets.get(j).getEnemy(k)) && fleets.get(j).getEnemy(k).alive && !shipBullets.get(i).struck)
                    {
                        fleets.get(j).getEnemy(k).alive = false;
                        fleets.get(j).getEnemy(k).deathSpotX = fleets.get(j).getEnemy(k).getLoop().getX();
                        fleets.get(j).getEnemy(k).deathSpotY = fleets.get(j).getEnemy(k).getLoop().getY();
                        shipBullets.get(i).struck = true;
                        score+=100;
                        System.out.println("Enemy Struck!");
                        
                    }
                }
            }
        }
        
        //The following loops determine if the player can move to the next level
        int enemiesDead = 0;
        
        for(int i = 0; i < fleets.size(); i++)
        {
            for(int j = 0; j < fleets.get(i).getNum(); j++)
            {
                if(!fleets.get(i).getEnemy(j).alive)
                {
                    enemiesDead++;
                }

            }
        }
        int totalEnemies = 0;
        for(int i = 0; i < fleets.size(); i++)
        {
            totalEnemies+=fleets.get(i).getNum();
        }
        
        if(enemiesDead == totalEnemies)
        {
            shipHit = false; 
            inGame = false;
            pregame = false;
            changeLevel = true;
        }

    }
    
    //Checks if a ship got hit by an enemy bullet or enemy itself
    public void checkShipHit()
    {
        for(int i = 0; i < enemyBullets.size(); i++)
        {
            if(bulletContactShip(enemyBullets.get(i)) && !enemyBullets.get(i).struck && lives > 1)
            {
                enemyBullets.get(i).struck = true;
                System.out.println("Ship Struck!");
                lives--;
                shipHit = true; 
                inGame = false;
                changeLevel = false;
                pregame = false;
                gameOver = false;
            } 
            else if(bulletContactShip(enemyBullets.get(i)) && !enemyBullets.get(i).struck)
            {
                enemyBullets.get(i).struck = true;
                System.out.println("Ship Struck!");
                lives--;
                shipHit = false; 
                inGame = false;
                changeLevel = false;
                pregame = false;
                gameOver = true;
            }
            
            
        }
        
        for(int i = 0; i < fleets.size(); i++)
        {
            for(int j = 0; j < fleets.get(i).getNum(); j++)
            {
                if(enemyContactShip(fleets.get(i).getEnemy(j)) && fleets.get(i).getEnemy(j).alive && !fleets.get(i).getEnemy(j).hitShip && lives > 1)
                {
                    System.out.println("Ship Struck!");
                    lives--;
                    fleets.get(i).getEnemy(j).hitShip = true;
                    shipHit = true; 
                    inGame = false;
                    changeLevel = false;
                    pregame = false;
                    gameOver = false;
                } 
                else if(enemyContactShip(fleets.get(i).getEnemy(j)) && fleets.get(i).getEnemy(j).alive && !fleets.get(i).getEnemy(j).hitShip)
                {
                    System.out.println("Ship Struck!");
                    lives--;
                    fleets.get(i).getEnemy(j).hitShip = true;
                    shipHit = false; 
                    inGame = false;
                    changeLevel = false;
                    pregame = false;
                    gameOver = true;
                }
            }
        }
    }
    
    //Utilized in checkShipHit. Sees if an enemy overlaps with the ship
    private boolean enemyContactShip(Enemy en)
    {
        boolean inXRange;
        boolean inYRange;
        inXRange = (en.getXMin() < ship_x_min && en.getXMax() > ship_x_min) || (en.getXMin() > ship_x_min && en.getXMax() < ship_x_max) || (en.getXMin() < ship_x_max && en.getXMax() > ship_x_max);
        inYRange = (en.getYMin() < ship_y_min && en.getYMax() > ship_y_min) || (en.getYMin() > ship_y_min && en.getYMax() < ship_y_max) || (en.getYMin() < ship_y_max && en.getYMax() > ship_y_max);
        return inXRange && inYRange;
    }
    
    //Utilized in checkShipHit. Sees if a bullet overlaps with the ship
    private boolean bulletContactShip(Bullet bull)
    {
        boolean inXRange;
        boolean inYRange;
        inXRange = (bull.getXMin() < ship_x_min && bull.getXMax() > ship_x_min) || (bull.getXMin() > ship_x_min && bull.getXMax() < ship_x_max) || (bull.getXMin() < ship_x_max && bull.getXMax() > ship_x_max);
        inYRange = (bull.getYMin() < ship_y_min && bull.getYMax() > ship_y_min) || (bull.getYMin() > ship_y_min && bull.getYMax() < ship_y_max) || (bull.getYMin() < ship_y_max && bull.getYMax() > ship_y_max);
        return inXRange && inYRange;
    }
    
    //Utilized in checkEnemyDeaths. Checks if a ship bullet overlaps with an enemy
    private boolean bulletContactEnemy(Bullet bull, Enemy en)
    {
        boolean inXRange;
        boolean inYRange;

        inXRange = (bull.getXMin() < en.getXMin() && bull.getXMax() > en.getXMin()) || (bull.getXMin() > en.getXMin() && bull.getXMax() < en.getXMax()) || (bull.getXMin() < en.getXMax() && bull.getXMax() > en.getXMax());
        inYRange = (bull.getYMin() < en.getYMin() && bull.getYMax() > en.getYMin()) || (bull.getYMin() > en.getYMin() && bull.getYMax() < en.getYMax()) || (bull.getYMin() < en.getYMax() && bull.getYMax() > en.getYMax());
        return inXRange && inYRange;
    }

    //This class gets actions from the keyboard
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            
            int key = e.getKeyCode();
            
            /*The following gates determine the functionality of
            pressing a key in a given game mode.
            */
            if(inGame)
            {
                if (key == KeyEvent.VK_LEFT) {
                    left = true;
                    right = false;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_RIGHT) {
                    left = false;
                    right = true;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_UP) {
                    left = false;
                    right = false;
                    up = true;
                    down = false;
                }

                if (key == KeyEvent.VK_DOWN) {
                    left = false;
                    right = false;
                    up = false;
                    down = true;
                }

                if (key == KeyEvent.VK_SPACE) {
                    shootBullet();
                }
            }
           
            if(changeLevel)
            {
                if (key == KeyEvent.VK_ENTER) {
                    for(int i = 0; i < fleets.size(); i++)
                    {
                        if((level+1)%10 == 0)
                            fleets.get(i).reset(10);
                        else
                            fleets.get(i).reset((level+1)%10);
                    }
                    if((level)%10 == 0 && level != 1)
                    {
                        fleets.add(new Fleet((int)(7*Math.random()), rd.nextBoolean(), 1));
                    }
                    enemyBullets.clear();
                    shipBullets.clear();
                    level++;
                    changeLevel = false;
                    inGame = true;
                    pregame = false;
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                }
            }
            if(pregame)
            {
                enemyBullets.clear();
                shipBullets.clear();
                if (key == KeyEvent.VK_ENTER)
                {
                    changeLevel = false;
                    inGame = true;
                    pregame = false;
                    for(int i = 0; i < fleets.size(); i++)
                    {
                        if((level+1)%10 == 0)
                            fleets.get(i).reset(10);
                        else
                            fleets.get(i).reset((level)%10);
                    }
                }
            }
            if(shipHit)
            {
                if (key == KeyEvent.VK_ENTER)
                {
                    changeLevel = false;
                    inGame = true;
                    pregame = false;
                    shipHit = false;
                    shipDeathParam = 0;
                    enemyBullets.clear();
                    shipBullets.clear();
                    for(int i = 0; i < fleets.size(); i++)
                    {
                        if((level)%10 == 0)
                            fleets.get(i).reset(10);
                        else
                            fleets.get(i).reset((level)%10);
                    }
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                    ship_x = 375;
                    ship_y = 600;
                }
            }
            
            if(gameOver)
            {
                if (key == KeyEvent.VK_ENTER)
                {
                    changeLevel = false;
                    inGame = false;
                    pregame = true;
                    shipHit = false;
                    gameOver = false;
                    shipDeathParam = 0;
                    enemyBullets.clear();
                    shipBullets.clear();
                    level = 1;
                    fleets.clear();
                    fleets.add(new Fleet((int)(7*Math.random()), rd.nextBoolean(), 1));
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                    ship_x = 375;
                    ship_y = 600;
                    lives = 3;
                    score = 0;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();
            /*The following gates determine the functionality of
            releasing a key in a given game mode. Its sole reason
            for existing is allowing the ship to move a bit more naturally.
            */
            if(inGame)
            {
                if (key == KeyEvent.VK_RIGHT) {
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_LEFT) {
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_UP) {
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_DOWN) {
                    left = false;
                    right = false;
                    up = false;
                    down = false;
                }
            }
        }
    }
}
