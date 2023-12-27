package com.app.orderapi.repository;

import com.app.orderapi.entities.Item;
import com.app.orderapi.entities.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepository extends JpaRepository<Pricing,Long> {
    Pricing findByItem(Item item);
}
