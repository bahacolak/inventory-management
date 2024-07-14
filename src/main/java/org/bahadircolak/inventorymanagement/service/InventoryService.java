package org.bahadircolak.inventorymanagement.service;

import org.bahadircolak.inventorymanagement.model.InventoryItem;
import org.bahadircolak.inventorymanagement.model.User;
import org.bahadircolak.inventorymanagement.repository.CategoryRepository;
import org.bahadircolak.inventorymanagement.repository.InventoryItemRepository;
import org.bahadircolak.inventorymanagement.repository.UserRepository;
import org.bahadircolak.inventorymanagement.web.advice.exception.UserNotFoundException;
import org.bahadircolak.inventorymanagement.web.request.InventoryItemRequest;
import org.bahadircolak.inventorymanagement.web.response.InventoryItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final InventoryItemRepository itemRepository;

    public InventoryService(CategoryRepository categoryRepository, UserRepository userRepository, InventoryItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    public InventoryItemResponse addItem(InventoryItemRequest itemRequest) {
        InventoryItem item = new InventoryItem();
        item.setName(itemRequest.getName());
        item.setQuantity(itemRequest.getQuantity());
        item.setPrice(itemRequest.getPrice());
        item.setCategory(categoryRepository.findById(itemRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        item.setUser(userRepository.findById(itemRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"))); // Eklendi

        InventoryItem newItem = itemRepository.save(item);
        return mapToInventoryItemResponse(newItem);
    }

    public List<InventoryItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToInventoryItemResponse)
                .collect(Collectors.toList());
    }

    public InventoryItemResponse getItemById(Long id) {
        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToInventoryItemResponse(item);
    }

    @Transactional
    public InventoryItemResponse updateItem(Long id, InventoryItemRequest itemRequest) {
        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(itemRequest.getName());
        item.setQuantity(itemRequest.getQuantity());
        item.setPrice(itemRequest.getPrice());
        item.setCategory(categoryRepository.findById(itemRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        item.setUser(userRepository.findById(itemRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        InventoryItem updatedItem = itemRepository.save(item);
        return mapToInventoryItemResponse(updatedItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public InventoryItemResponse mapToInventoryItemResponse(InventoryItem item) {
        InventoryItemResponse response = new InventoryItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setCategoryName(item.getCategory().getName());
        response.setUserName(item.getUser().getFirstName() + " " + item.getUser().getLastName()); // Eklendi
        return response;
    }

    public void applyDiscount(Long userId, double discountPercentage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.getItems().forEach(item -> {
            double discountedPrice = item.getPrice() * (1 - discountPercentage / 100);
            item.setPrice(discountedPrice);
            itemRepository.save(item);
        });
    }
}
