package org.bahadircolak.inventorymanagement.repository;

import org.bahadircolak.inventorymanagement.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
}

