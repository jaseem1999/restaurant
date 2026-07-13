package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.projection.offer.MenuOfferProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuOfferRepository extends JpaRepository<MenuOffer, Long> {

    @Query("SELECT m.id AS id, m.offerName AS offerName, m.description AS description, " +
            "m.offerType AS offerType, m.discountValue AS discountValue, " +
            "m.minimumOrderValue AS minimumOrderValue, m.startDate AS startDate, " +
            "m.endDate AS endDate, m.active AS active, mi.id AS menuItemId " +
            "FROM MenuOffer m JOIN m.menuItem mi WHERE mi.id = :menuItemId")
    List<MenuOfferProjection> findByMenuItemId(Long menuItemId);

    @Query("SELECT m.id AS id, m.offerName AS offerName, m.description AS description, " +
            "m.offerType AS offerType, m.discountValue AS discountValue, " +
            "m.minimumOrderValue AS minimumOrderValue, m.startDate AS startDate, " +
            "m.endDate AS endDate, m.active AS active, mi.id AS menuItemId " +
            "FROM MenuOffer m JOIN m.menuItem mi WHERE mi.id = :menuItemId AND m.active = true")
    List<MenuOfferProjection> findByMenuItemIdAndActiveTrue(Long menuItemId);

    @Query(
        "SELECT m.id AS id, m.offerName AS offerName, m.description AS description, " +
            "m.offerType AS offerType, m.discountValue AS discountValue, " +
            "m.minimumOrderValue AS minimumOrderValue, m.startDate AS startDate, " +
            "m.endDate AS endDate, m.active AS active, mi.id AS menuItemId " +
            "FROM MenuOffer m JOIN m.menuItem mi WHERE mi.id = :menuItemId AND m.active = true AND m.endDate > :currentDate"
    )
    List<MenuOfferProjection> findByMenuItemIdAndActiveTrueAndEndDateAfter(Long menuItemId, Instant currentDate);

    @Query(
        "SELECT m.id AS id, m.offerName AS offerName, m.description AS description, " +
            "m.offerType AS offerType, m.discountValue AS discountValue, " +
            "m.minimumOrderValue AS minimumOrderValue, m.startDate AS startDate, " +
            "m.endDate AS endDate, m.active AS active, mi.id AS menuItemId " +
            "FROM MenuOffer m JOIN m.menuItem mi WHERE m.active = true AND m.startDate < :startDate AND m.endDate > :endDate"
    )
    List<MenuOfferProjection> findByActiveTrueAndStartDateBeforeAndEndDateAfter(Instant startDate, Instant endDate);

    boolean existsByOfferNameAndMenuItemId(String offerName, Long menuItemId);

    @Query(
    "SELECT m.id AS id, m.offerName AS offerName, m.description AS description, " +
            "m.offerType AS offerType, m.discountValue AS discountValue, " +
            "m.minimumOrderValue AS minimumOrderValue, m.startDate AS startDate, " +
            "m.endDate AS endDate, m.active AS active, mi.id AS menuItemId " +
            "FROM MenuOffer m JOIN m.menuItem mi where m.id =:id"
    )
    Optional<MenuOfferProjection> findByIdProjection(Long id);
}

