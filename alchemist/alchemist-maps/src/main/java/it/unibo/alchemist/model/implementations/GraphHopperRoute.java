/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.util.PointList;

import it.unibo.alchemist.model.implementations.positions.LatLongPosition;
import it.unibo.alchemist.model.interfaces.Route;
import it.unibo.alchemist.model.interfaces.Position;
import java.util.stream.Collectors;

/**
 */
public final class GraphHopperRoute implements Route {

    private static final long serialVersionUID = -1455332156736222268L;
    private final int size;
    private final double distance, time;
    private final List<Position> points;

    /**
     * @param response
     *            the response to use
     */
    public GraphHopperRoute(final GHResponse response) {
        final List<Throwable> errs = response.getErrors();
        if (errs.isEmpty()) {
            final PathWrapper resp = response.getBest();
            time = resp.getTime() / 1000d;
            distance = resp.getDistance();
            final PointList pts = resp.getPoints();
            size = pts.getSize();
            final List<Position> temp = new ArrayList<>(size);
            for (int i = 0; i < pts.getSize(); i++) {
                temp.add(new LatLongPosition(pts.getLatitude(i), pts.getLongitude(i)));
            }
            points = Collections.unmodifiableList(temp);
        } else {
            final String msg = errs.stream().map(Throwable::getMessage).collect(Collectors.joining("\n"));
            throw new IllegalArgumentException(msg, errs.get(0));
        }
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public Position getPoint(final int step) {
        return points.get(step);
    }

    @Override
    public List<Position> getPoints() {
        return points;
    }

    @Override
    public int getPointsNumber() {
        return size;
    }

    @Override
    public double getTime() {
        return time;
    }

}
