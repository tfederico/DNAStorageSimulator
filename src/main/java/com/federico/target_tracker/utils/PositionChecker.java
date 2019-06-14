package com.federico.target_tracker.utils;

import com.federico.target_tracker.bionanothings.Beacon;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Created by federico on 26/05/16.
 */
/**
 *Class used to make geometric operations, for example checking if a point is placed inside a triangle
 */
public class PositionChecker {

    /**
     * Method that checks on which side of the half-plane created by the edges the point is
     * @param p1 First point used to check on which side of the half-plane created by the edges the point is
     * @param p2 Second point used to check on which side of the half-plane created by the edges the point is
     * @param p3 Third point used to check on which side of the half-plane created by the edges the point is
     * @return  double
     */
    private static double sign(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
    }

    /**
     * Method that tells if a point is situated inside of a triangle
     * @param point Point that could be inside of the triangle
     * @param v0 First vertex of the triangle
     * @param v1 Second vertex of the triangle
     * @param v2 Third vertex of the triangle
     * @return  boolean
     */
    public static boolean pointInTriangle(Point2D point, Point2D v0, Point2D v1, Point2D v2) {

        boolean b1, b2, b3;

        b1 = sign(point, v0, v1) < 0.0d;
        b2 = sign(point, v1, v2) < 0.0d;
        b3 = sign(point, v2, v0) < 0.0d;

        return ((b1 == b2) && (b2 == b3));
    }

    public static boolean pointInPolygon(List<Beacon> beacons, Point2D test) {
        int nvert = beacons.size();
        int i, j = 0;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((beacons.get(i).getPosition().getY()>test.getY()) !=
                    (beacons.get(j).getPosition().getY()>test.getY())) &&
                    (test.getX() < (beacons.get(j).getPosition().getX()-beacons.get(i).getPosition().getX()) *
                            (test.getY()-beacons.get(i).getPosition().getY()) /
                            (beacons.get(j).getPosition().getY()-beacons.get(i).getPosition().getY()) +
                            beacons.get(i).getPosition().getX()) )
                c = !c;
        }

        return c;
    }

    public static boolean beaconsInArea(List<Beacon> beacons, double size){
        boolean in = true;
        for(Beacon b : beacons){
            in = in && (b.getPosition().getX()<=size/2 && b.getPosition().getX()>=-size/2 &&
                    b.getPosition().getY()<=size/2 && b.getPosition().getY()>=-size/2);
        }
        return in;
    }

}
