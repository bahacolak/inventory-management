package org.bahadircolak.inventorymanagement.web.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemRequest {
    private String name;
    private int quantity;
    private double price;
    private Long categoryId;
    private Long userId;
}

