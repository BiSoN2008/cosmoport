package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import static com.space.service.CheckedFieldsValue.checkId;


@RestController
@RequestMapping(value = "/rest")
public class ShipController {
    private final Logger logger = LoggerFactory.getLogger(ShipController.class);
    private ShipService shipService;

    @GetMapping(value = "/ships", produces = "application/json")
    public List<Ship> getListShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRaring", required = false) Double maxRating,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "order", defaultValue = "ID") ShipOrder order) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return shipService.findShipsByParams(pageable, name, planet, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, shipType);
    }

    @DeleteMapping(value = "/ships/{id}", produces = "application/json")
    public void deleteShip(@PathVariable(value = "id") Long id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (checkId(id, response, request)) return;
        try {
            shipService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            try {
                response.sendError(404);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/ships/count", produces = "application/json")
    public Integer getCountShips(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "planet", required = false) String planet,
                                 @RequestParam(value = "shipType", required = false) ShipType shipType,
                                 @RequestParam(value = "after", required = false) Long after,
                                 @RequestParam(value = "before", required = false) Long before,
                                 @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                 @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                 @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                 @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                 @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                 @RequestParam(value = "minRating", required = false) Double minRating,
                                 @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return shipService.findShipsByParams(Pageable.unpaged(), name, planet,after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,shipType).size();
    }

    @PostMapping(value = "/ships", produces = "application/json")
    public Ship createShip(@RequestBody Ship ship, HttpServletResponse response) {
        try {
            return shipService.save(ship);
        } catch (IllegalArgumentException | NullPointerException e) {
            try {
                response.sendError(400);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @GetMapping(value = "/ships/{id}", produces = "application/json")
    public Ship getShip(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
        if (checkId(id, response, request)) return null;
        Ship ship = shipService.findById(id);
        if (ship != null) {
            return ship;
        } else {
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @PostMapping(value = "/ships/{id}", produces = "application/json")
    public Ship updateShip(@RequestBody Ship ship, @PathVariable Long id,
                           HttpServletResponse response, HttpServletRequest request) {
        if (checkId(id, response, request)) return null;
        Ship shipFromDB = shipService.findById(id);
        if (shipFromDB == null) {
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        if (ship == null) {
            return shipFromDB;
        }
        try {
            return shipService.update(ship, id);
        } catch (IllegalArgumentException | NullPointerException e) {
            try {
                response.sendError(400);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }


}
