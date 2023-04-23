/*
class Loop.
Represents a path an enemy takes.
 */
package gradleproject1;

public class Loop {

    private int type; //type of loop
    private boolean mirror; //determines if loop is reversed or not
    private int[] xArr; //x coordinates of the control points
    private int[] yArr; //y coordinates of the control points
    private double t = 0.0; //parameter determining where the enemy is in the loop
    private final int width = 750; //width of board
    public int numPoints; //number of control points
    private double[][] controlPoints; // 2d array of control points

    //Constructor. Creates a loop given a loop type and if it is reversed
    public Loop(int typeLoop, boolean mir) {
        type = typeLoop;
        mirror = mir;
        setControlPoints();
        t = 0;

    }

    //returns parameter of the loop
    public double getT() {
        return t;
    }
    
    //resets the loop back to 0
    public void resetT()
    {
        t = 0;
    }

    //Sets control points for a specific type of loop
    private void setControlPoints() {
        //type 0: loop-de-loop in the middle of the screen
        if (type == 0) {
            numPoints = 5;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 0;
            xArr[1] = 450;
            yArr[1] = 300;
            xArr[2] = 375;
            yArr[2] = 400;
            xArr[3] = 300;
            yArr[3] = 300;
            xArr[4] = 800;
            yArr[4] = 0;
        }
        
        //type 1: horizontal line across the top of the screen
        if(type == 1)
        {
            numPoints = 2;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 30;
            xArr[1] = 800;
            yArr[1] = 30;
        }
        
        //type 2: Hyperbolic path
        if(type == 2)
        {
            numPoints = 3;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 20;
            xArr[1] = 375;
            yArr[1] = 375;
            xArr[2] = -50;
            yArr[2] = 550;
        }
        
        //type 3: Diagonal line across the screen
        if(type == 3)
        {
            numPoints = 2;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 300;
            xArr[1] = 800;
            yArr[1] = 600;
        }
        
        //Type 4: Parabolic path
        if(type == 4)
        {
            numPoints = 3;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 0;
            xArr[1] = 375;
            yArr[1] = 500;
            xArr[2] = 800;
            yArr[2] = 0;
        }
        
        //type 5: Odd horseshoe shape. Randomly selected points by myself
        if(type == 5)
        {
            numPoints = 8;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 400;
            xArr[1] = 300;
            yArr[1] = 370;
            xArr[2] = 300;
            yArr[2] = 200;
            xArr[3] = 150;
            yArr[3] = 150;
            xArr[4] = 100;
            yArr[4] = 250;
            yArr[5] = 400;
            xArr[5] = 300;
            yArr[6] = 370;
            xArr[6] = 300;
            xArr[7] = -50;
            yArr[7] = 0;
            
        }
        
        //type 6: resembles a trigonometric wave
        if(type == 6)
        {
            numPoints = 5;
            xArr = new int[numPoints];
            yArr = new int[numPoints];
            controlPoints = new double[numPoints][3];

            xArr[0] = -50;
            yArr[0] = 150;
            xArr[1] = 188;
            yArr[1] = 500;
            xArr[2] = 375;
            yArr[2] = 150;
            xArr[3] = 563;
            yArr[3] = 500;
            xArr[4] = 800;
            yArr[4] = 150;
        }

            for (int i = 0; i < xArr.length; i++) {
                double tInc = 1 / (double) (numPoints - 1);
                controlPoints[i][0] = i * tInc;
            }

            for (int i = 0; i < xArr.length; i++) {
                if (mirror) 
                {
                    controlPoints[i][1] = (double) (xArr[i] + (2 * ((width / 2) - xArr[i])));
                    controlPoints[i][2] = (double) yArr[i];
                } 
                else 
                {
                    controlPoints[i][1] = (double) xArr[i];
                    controlPoints[i][2] = (double) yArr[i];
                }
            }

        
    }

    //returns the x coordinate of the path
    public int getX() {
        return xOutput();
    }

    //returns the y coordinate of the path
    public int getY() {
        return yOutput();
    }

    //runs the loop
    public void runLoop() {
        if (t < 1) {
            t += .004; //this is experimentally determined to make the enemy the perfect speed
        }
    }
    
    

    //generates a y on the path using interpolation of the control points
    private int yOutput() {
        int y = 0;
        double term;
        for (int i = 0; i < controlPoints.length; i++)//index of sum
        {
            term = controlPoints[i][2];
            for (int j = 0; j < controlPoints.length; j++)//index of multiplication
            {

                if (j != i) {
                    term *= ((t - controlPoints[j][0]) / (controlPoints[i][0] - controlPoints[j][0]));
                }

            }

            y += (int) term;
            term = 0;

        }
        return y;
    }
    
    //generates an x on the path using interpolation of the control points
    private int xOutput() {
        int x = 0;
        double term;
        for (int i = 0; i < controlPoints.length; i++)//index of sum
        {
            term = controlPoints[i][1];
            for (int j = 0; j < controlPoints.length; j++)//index of multiplication
            {

                if (j != i) {
                    term *= ((t - controlPoints[j][0]) / (controlPoints[i][0] - controlPoints[j][0]));
                }

            }

            x += (int) term;
            term = 0;

        }
        return x;
    }
}
