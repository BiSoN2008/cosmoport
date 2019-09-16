package com.space.service;

import com.space.model.Ship;
import org.apache.commons.math3.util.Precision;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class CheckedFieldsValue {

    public static boolean checkName(Ship ship) {
        boolean result = true;
        if (ship.getName().isEmpty() || ship.getName().length() > 50) result = false;
        return result;
    }

    public static boolean checkPlanet(Ship ship) {
        boolean result = true;
        if (ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50) result = false;
        return result;
    }

    public static boolean checkType(Ship ship) {
        boolean result = true;
        if (ship.getShipType() == null) result = false;
        return result;
    }

    public static boolean checkDate(Ship ship) {
        int year = year(ship);
        boolean result = true;
        if (year < 2800 || year > 3019) result = false;
        return result;
    }

    public static boolean checkUsed(Ship ship) {
        boolean result = true;
        if (ship.getIsUsed() == null) result = false;
        return result;
    }

    public static boolean checkSpeed(Ship ship) {
        boolean result = true;
        if (ship.getSpeed() == 0) return false;
        double speed = Precision.round(ship.getSpeed(), 2);
        if (speed < 0.01 || speed > 0.99) result = false;
        return result;
    }

    public static boolean checkCrewSize(Ship ship) {
        boolean result = true;
        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) result = false;
        return result;
    }

    public static int year(Ship ship) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        return calendar.get(Calendar.YEAR);
    }

    public static double getRating(Ship ship, int year) {
        return Precision.round((80 * ship.getSpeed() * (ship.getIsUsed() ? 0.5 : 1)) / (3019 - year + 1), 2);
    }

    public static boolean checkId(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
        String[] uri = request.getRequestURI().split("/");
        try {
            Long param = Long.parseLong(uri[uri.length - 1]);
        } catch (NumberFormatException e) {
            try {
                response.sendError(400);
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (id <= 0) {
            try {
                response.sendError(400);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
