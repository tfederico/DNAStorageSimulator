package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Abstract class that describes the default beahviour of a movement algorithm which is made of two main steps: moving the receiver and computing the drift angle
 */
public abstract class MovementAlgorithm {

    /**
     * Speed of the receivers
     */
    protected final double speed;

    /**
     * Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     */
    protected final double D;

    /**
     * Delta time (in seconds) that divides one step of the simulation from the previous one
     */
    protected final double deltaT;

    /**
     * Length of the square side in which takes place the simulation
     */
    protected final double length;

    /**
     * Coefficient that defines the degree of disturbance
     */
    protected final double gamma;

    /**
     * Random angle which changes every deltaT
     */
    protected double theta = Math.random()*(2*Math.PI);

    /**
     * Number of angles used to check attractant and repellents concentration during a step of the simulation
     */
    protected static final int CHECKED_ANGLES = 5;

    /**
     * Boolean variable that tells if the receiver should move or not
     */
    private boolean shouldMove = false;

    /**
     * Constructor of the MovementAlgorithm class
     * @param speed Speed of the receivers
     * @param D Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     * @param deltaT Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L Side of the square in which the simulation takes place
     * @param gamma Coefficient that defines the degree of disturbance
     */
    public MovementAlgorithm(double speed, double D, double deltaT, double L, double gamma){
        this.speed = speed;
        this.D = D;
        this.deltaT = deltaT;
        this.length = L;
        this.gamma = gamma;
        this.shouldMove = true;
    }

    /**
     * Method that moves the receiver of one step at every iteration of the single simulation
     * @param startPosition Position where the receiver is located
     * @return  Point2D
     */
    public Point2D moveOneStep(Point2D startPosition){
        if (shouldMove){
            double newX = startPosition.getX() + speed*deltaT*Math.cos(theta);
            double newY = startPosition.getY() + speed*deltaT*Math.sin(theta);

            //Receiver is restricted within a box
            if(newX > length/2) {
                newX = length/2 - (newX - length/2);
                theta = theta + Math.random()*2*Math.PI;
            }
            else if(newX < -length/2) {
                newX = -length/2 - (newX + length/2);
                theta = theta + Math.random()*2*Math.PI;
            }

            if(newY > length/2) {
                newY = length/2 - (newY - length/2);
                theta = theta + Math.random()*2*Math.PI;
            }
            else if(newY < -length/2) {
                newY = -length/2 - (newY + length/2);
                theta = theta + Math.random()*2*Math.PI;
            }
            return new Point2D(newX, newY);
        }
        else
            return startPosition;
    }

    /**
     * Method that calculate the drift angle of the receiver. It uses findAttractantDriftAngle and findRepellentDriftAngle
     * @param beacons Ensemble of the beacons used during the simulation
     * @param psi_a Attractant inducted angle  (expressed in rad)
     * @param psi_r Repellent inducted angle  (expressed in rad)
     * @param position Position from where calculate the next drift angle
     */
    public void computeAngle(ArrayList<Beacon> beacons, double psi_a, double psi_r, Point2D position){

        double attractantDriftAngle = findAttractantDriftAngle(beacons, position, psi_a); //PSI_a
        double repellentDriftAngle = findRepellentDriftAngle(beacons, position, psi_r); //PSI_r

        //Randomly choose a "+" or "-" sign
        Random random = new Random();
        int randomSignum = (random.nextInt(2) - 1);
        if(randomSignum == 0)
            randomSignum = 1;

        //Sum up the old angle with the rotational diffusion one
        //Sum up the drift angles with the rotational diffusion angle
        double dTheta = attractantDriftAngle + repellentDriftAngle + randomSignum*Math.sqrt(D*2*deltaT);
        theta += dTheta;
    }

    /**
     * Method that calculate the drift angle of the receiver caused by attractants
     * @param beacons Ensemble of the beacons used during the simulation
     * @param position Position from where calculate the next attractant drift angle
     * @param psi_a Attractant inducted angle  (expressed in rad)
     * @return  double
     */
    protected double findAttractantDriftAngle(ArrayList<Beacon> beacons, Point2D position, double psi_a){

        double maxAttractant = 0;
        double attractantDriftAngle = 0;
        //Find max attractant concentration, checking at checkedAngles different angles
        for(int i = 0; i < CHECKED_ANGLES; i++) {
            double currentSlice = -psi_a + psi_a*2*i/(CHECKED_ANGLES-1);
            double testX = position.getX() + speed*deltaT*Math.cos(theta + currentSlice);
            double testY = position.getY() + speed*deltaT*Math.sin(theta + currentSlice);
            //abstract method
            double result = checkAttractantConcentration(beacons, testX, testY);

            if(result > maxAttractant) {
                maxAttractant = result;
                attractantDriftAngle = currentSlice;
            }
        }
        return attractantDriftAngle;
    }

    /**
     * Method that calculate the drift angle of the receiver caused by repellents
     * @param beacons Ensemble of the beacons used during the simulation
     * @param position Position from where calculate the next repellent drift angle
     * @param psi_r Repellent inducted angle  (expressed in rad)
     * @return  double
     */
    protected double findRepellentDriftAngle(ArrayList<Beacon> beacons, Point2D position, double psi_r){

        double minRepellent = Double.MAX_VALUE;
        double repellentDriftAngle = 0;
        //Find max attractant concentration, checking at checkedAngles different angles
        //Find min repellent concentration, checking at checkedAngles different angles
        for(int i = 0; i < CHECKED_ANGLES; i++) {
            double currentSlice = -psi_r + psi_r*2*(i+1)/(CHECKED_ANGLES-1);
            double testX = position.getX() + speed*deltaT*Math.cos(theta + currentSlice);
            double testY = position.getY() + speed*deltaT*Math.sin(theta + currentSlice);
            //abstract method
            double result = checkRepellentConcentration(beacons, testX, testY);

            if(result < minRepellent) {
                minRepellent = result;
                repellentDriftAngle = currentSlice;
            }
        }
        return repellentDriftAngle;
    }

    /**
     * Method that calculate the concentration of repellents in the given point
     * @param beacons Ensemble of the beacons used during the simulation
     * @param x Position on the x-axis of the point from where calculate the repellent concentration
     * @param y Position on the y-axis of the point from where calculate the repellent concentration
     * @return  double
     */
    protected abstract double checkRepellentConcentration(ArrayList<Beacon> beacons, double x, double y);

    /**
     * Method that calculate the concentration of attractants in the given point
     * @param beacons Ensemble of the beacons used during the simulation
     * @param x Position on the x-axis of the point from where calculate the attractant concentration
     * @param y Position on the x-axis of the point from where calculate the attractant concentration
     * @return  double
     */
    protected abstract double checkAttractantConcentration(ArrayList<Beacon> beacons, double x, double y);

    /**
     * Method that tells to the receiver to move or to stop
     * @param shouldMove Boolean variable which tells if the receiver should mover or not
     */
    public void shouldMove(boolean shouldMove){
        this.shouldMove = shouldMove;
    }
}
