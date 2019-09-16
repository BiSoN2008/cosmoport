package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.space.service.CheckedFieldsValue.*;

@Service
public class ShipServiceImpl implements ShipService {
    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Ship findById(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ship save(Ship ship) {
        int year = year(ship);
        if (ship.getIsUsed() == null) ship.setIsUsed(false);

        if (ship.getName() == null || ship.getPlanet() == null || ship.getProdDate() == null ||
                ship.getSpeed() == null || ship.getCrewSize() == null) throw new IllegalArgumentException();
        if (!checkName(ship) || !checkPlanet(ship) || !checkType(ship) || !(checkDate(ship)) || !(checkUsed(ship)) ||
                !checkSpeed(ship) || !checkCrewSize(ship)) throw new IllegalArgumentException();

        ship.setSpeed(Precision.round(ship.getSpeed(), 2));
        ship.setRating(getRating(ship, year));
        return shipRepository.save(ship);
    }

    @Override
    @Transactional
    public Ship update(Ship ship, Long id) {
        Ship updateShip = shipRepository.findById(id).get();
        if (ship.getName() != null) {
            if (!checkName(ship)) throw new IllegalArgumentException();
            updateShip.setName(ship.getName());
        }
        if (ship.getPlanet() != null) {
            if (!checkPlanet(ship)) throw new IllegalArgumentException();
            updateShip.setPlanet(ship.getPlanet());
        }
        if (checkType(ship)) updateShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null) {
            if (!checkDate(ship)) throw new IllegalArgumentException();
            updateShip.setProdDate(ship.getProdDate());
        }
        if (checkUsed(ship)) updateShip.setIsUsed(ship.getIsUsed());
        if (ship.getSpeed() != null) {
            if (!checkSpeed(ship)) throw new IllegalArgumentException();
            updateShip.setSpeed(Precision.round(ship.getSpeed(), 2));
        }
        if (ship.getCrewSize() != null) {
            if (!checkCrewSize(ship)) throw new IllegalArgumentException();
            updateShip.setCrewSize(ship.getCrewSize());
        }
        updateShip.setRating(getRating(updateShip, year(updateShip)));
        return shipRepository.save(updateShip);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Ship> findShipsByParams(Pageable pageable, String name,
                                        String planet, Long after, Long before,
                                        Boolean isUsed, Double minSpeed, Double maxSpeed,
                                        Integer minCrewSize, Integer maxCrewSize,
                                        Double minRating, Double maxRating, ShipType shipType) {

        Date dateAfter = null;
        Date dateBefore = null;
        if (after != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(after);
            dateAfter = calendar.getTime();
        }
        if (before != null) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(before);
            dateBefore = calendar1.getTime();
        }
        return shipRepository.findByParams(name, planet, dateAfter, dateBefore, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, shipType, pageable);
    }
}
