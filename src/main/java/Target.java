import java.util.Random;

public class Target {

     double[] pos;
    private static int id;

    private boolean targetAcquired;
    public Target(int[][] angle){
        id++;
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
