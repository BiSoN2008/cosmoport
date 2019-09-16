package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ShipService {
    Ship findById(Long id);
    void deleteById(Long id);
    Ship save(Ship ship);
    Ship update(Ship ship, Long id);
    List<Ship> findShipsByParams(Pageable pageable, String name, String planet, Long after, Long before,
                                 Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                 Integer maxCrewSize, Double minRating, Double maxRating, ShipType shipType);

}
