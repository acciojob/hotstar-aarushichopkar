package com.driver.repository;

import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    List<WebSeries> findAllByAgeLimitLessThanEqualAndSubscriptionType(Integer ageLimit , SubscriptionType subscriptionType);
}
