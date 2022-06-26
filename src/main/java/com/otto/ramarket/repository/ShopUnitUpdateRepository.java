package com.otto.ramarket.repository;

import com.otto.ramarket.entity.ShopUnitUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShopUnitUpdateRepository extends JpaRepository<ShopUnitUpdate,
        Long> {
    List<ShopUnitUpdate> findAllByShopUnitIdAndUpdateDateGreaterThanEqualAndUpdateDateLessThan(UUID id, Instant dateStart, Instant dateEnd);
}
