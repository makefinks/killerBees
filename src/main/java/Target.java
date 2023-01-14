import java.util.ArrayList;
import java.util.Random;

public class Target {

    double[] pos;
    private static int id = 0;
    private boolean targetAcquired;

    private int life = 0;
    //Double[][] winkel; brauchen targets winkel?

    public Target(Double[][] winkel, ArrayList<Integer[]> targetPositions, int anzToDestroy){
        this.pos = new double[2];
        this.life = anzToDestroy;
        //this.winkel = winkel;
        //pos[0] = 0;
        //pos[1] = 0;
        pos[0] = targetPositions.get(id)[0]; //+ Simulation.pix * 50 * Math.random()
        pos[1] = targetPositions.get(id)[1];
        id++;
    }

    /*
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

     */
    public void reduceLife() {
        life--;
    }

    public int getLife() {
        return life;
    }
}
