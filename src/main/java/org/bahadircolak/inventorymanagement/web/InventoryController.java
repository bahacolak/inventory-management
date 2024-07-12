package org.bahadircolak.inventorymanagement.web;

import org.bahadircolak.inventorymanagement.service.InventoryService;
import org.bahadircolak.inventorymanagement.web.request.InventoryItemRequest;
import org.bahadircolak.inventorymanagement.web.response.InventoryItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/items")
    public ResponseEntity<InventoryItemResponse> addItem(@RequestBody InventoryItemRequest itemRequest) {
        InventoryItemResponse newItem = inventoryService.addItem(itemRequest);
        return ResponseEntity.ok(newItem);
    }

    @GetMapping("/items")
    public ResponseEntity<List<InventoryItemResponse>> getAllItems() {
        List<InventoryItemResponse> items = inventoryService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<InventoryItemResponse> getItemById(@PathVariable Long id) {
        InventoryItemResponse item = inventoryService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<InventoryItemResponse> updateItem(@PathVariable Long id, @RequestBody InventoryItemRequest itemRequest) {
        InventoryItemResponse updatedItem = inventoryService.updateItem(id, itemRequest);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
