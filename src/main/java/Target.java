import java.util.Random;

public class Target {

    double[] pos;
    private static int id;
    private boolean targetAcquired;
    Double[][] winkel;

    public Target(Double[][] winkel){
        this.pos = new double[2];
        id++;
        this.winkel = winkel;
        pos[0] = 0;
        pos[1] = 0;
    }

    public void move(){

        //calculate new dest pos of vehicle
        Random rand = new Random();
        int xoffset = rand.nextInt(-2, 2);
        int yoffset = rand.nextInt(-2, 2);

        //check if new Position is valid
        //Walls or out of bounds? if true find other possible solution

        //set pos
        pos[0] = pos[0] + xoffset;
        pos[1] = pos[1] + yoffset;


    }
}
