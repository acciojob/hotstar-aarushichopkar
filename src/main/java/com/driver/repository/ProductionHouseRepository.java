package com.driver.repository;

import com.driver.model.ProductionHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductionHouseRepository extends JpaRepository<ProductionHouse,Integer> {

    @Query("SELECT AVG(w.rating) FROM WebSeries w WHERE w.productionHouse.id = :productionHouseId")
    double getAverageRatingByProductionHouseId(@Param("productionHouseId") Integer productionHouseId);
}
