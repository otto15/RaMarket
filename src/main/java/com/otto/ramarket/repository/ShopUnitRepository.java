package com.otto.ramarket.repository;

import com.otto.ramarket.entity.ShopUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnit, UUID> {

}
