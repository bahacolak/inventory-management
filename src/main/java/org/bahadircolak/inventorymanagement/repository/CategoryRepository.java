package org.bahadircolak.inventorymanagement.repository;

import org.bahadircolak.inventorymanagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

