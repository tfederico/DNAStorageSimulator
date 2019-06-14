package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.PositionChecker;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;

import java.util.Random;

/**
 * Created by federico on 27/07/16.
 */
public class SquareSimulationCase extends DestinationSimulationCase {
    /**
     * Constructor of the DestinationSimulationCase class
     *  @param logger  Logger used to store the simulation data
     * @param inOrOut Argument that defines if the destination must be placed inside or outside the triangle plotted by beacons
     */
    public SquareSimulationCase(Logger logger, boolean inOrOut) {
        super(logger, inOrOut);
    }

    @Override
    protected void placeBeacons() throws ImpossibleException {

        Point2D p0 = new Point2D(Math.random()-L/2, Math.random()-L/2);
        Point2D p1 = new Point2D(Math.random()-L/2, Math.random()-L/2);
        while(p0.distance(p1)<0.01)
            p1 = new Point2D(Math.random()-L/2, Math.random()-L/2);
        double height = 0;
        while(height < 0.01)
            height = Math.abs(Math.random()-L/2);

        double m = (p0.getY()-p1.getY())/(p0.getX()-p1.getX());
        if(p0.getX() < p1.getX() && p0.getY() > p1.getY())
            m = -m;

        double beta = Math.toDegrees(Math.atan(m));

        if(beta < 0)
            beta += 360;

        beta = Math.toRadians((beta+90)%360);

        Point2D p2 = new Point2D(p0.getX()+Math.cos(beta)*height,
                p0.getY()+Math.sin(beta)*height);
        Point2D p3 = new Point2D(p1.getX()+Math.cos(beta)*height,
                p1.getY()+Math.sin(beta)*height);


        beaconsList.add(new Beacon(receivers + 3 , p0.getX(), p0.getY()));
        beaconsList.add(new Beacon(receivers + 3 + 1, p2.getX(), p2.getY()));
        beaconsList.add(new Beacon(receivers + 3 + 2, p3.getX(), p3.getY()));
        beaconsList.add(new Beacon(receivers + 3 + 3, p1.getX(), p1.getY()));

        for(Beacon b : beaconsList){
            b.emitAttractants(shouldAllEmitsAttractans);
            b.emitRepellents(shouldAllEmitsRepellents);
        }

        if(!PositionChecker.beaconsInArea(beaconsList,L)){
            beaconsList.clear();
            placeBeacons();
        }


    }
}
