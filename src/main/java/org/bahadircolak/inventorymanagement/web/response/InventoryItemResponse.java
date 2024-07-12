package org.bahadircolak.inventorymanagement.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemResponse {
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private String categoryName;
}

