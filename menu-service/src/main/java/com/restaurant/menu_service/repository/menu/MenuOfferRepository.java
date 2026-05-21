package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MenuOfferRepository extends JpaRepository<MenuOffer, Long> {
    
    List<MenuOffer> findByMenuItemId(Long menuItemId);
    
    List<MenuOffer> findByMenuItemIdAndActiveTrue(Long menuItemId);
    
    List<MenuOffer> findByMenuItemIdAndActiveTrueAndEndDateAfter(Long menuItemId, LocalDateTime currentDate);
    
    List<MenuOffer> findByActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByOfferNameAndMenuItemId(String offerName, Long menuItemId);
}

