package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface ShipRepository extends PagingAndSortingRepository<Ship,Long> {

    @Query(value = "select s from Ship s where (?1 is null or s.name like %?1%) and (?2 is null or s.planet like %?2%) " +
            "and (?3 is null or s.prodDate > ?3) and (?4 is null or s.prodDate < ?4) and  (?5 is null or s.isUsed = ?5) " +
            "and (?6 is null or s.speed >= ?6) and (?7 is null or s.speed <= ?7) and (?8 is null or s.crewSize >= ?8) " +
            "and (?9 is null or s.crewSize <= ?9) and (?10 is null or s.rating >= ?10) and (?11 is null or s.rating <= ?11)" +
            "and (?#{[11]} is null or s.shipType = ?#{[11]} )")



    List<Ship> findByParams(String name, String planet, Date after, Date before, Boolean isUsed, Double minSpeed,
                            Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                            Double maxRating, ShipType shipType, Pageable pageable);
}
