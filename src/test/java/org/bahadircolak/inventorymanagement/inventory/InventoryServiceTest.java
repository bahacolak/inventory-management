package org.bahadircolak.inventorymanagement.inventory;

import org.bahadircolak.inventorymanagement.model.Category;
import org.bahadircolak.inventorymanagement.model.InventoryItem;
import org.bahadircolak.inventorymanagement.model.User;
import org.bahadircolak.inventorymanagement.repository.CategoryRepository;
import org.bahadircolak.inventorymanagement.repository.InventoryItemRepository;
import org.bahadircolak.inventorymanagement.repository.UserRepository;
import org.bahadircolak.inventorymanagement.service.InventoryService;
import org.bahadircolak.inventorymanagement.web.request.InventoryItemRequest;
import org.bahadircolak.inventorymanagement.web.response.InventoryItemResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryItemRepository itemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private InventoryItem item;
    private InventoryItemRequest itemRequest;
    private User user;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Sample Category");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        item = new InventoryItem();
        item.setId(1L);
        item.setName("Item 1");
        item.setQuantity(10);
        item.setPrice(100.0);
        item.setCategory(category);
        item.setUser(user);

        itemRequest = new InventoryItemRequest();
        itemRequest.setName("Updated Item");
        itemRequest.setQuantity(5);
        itemRequest.setPrice(150.0);
        itemRequest.setCategoryId(1L);
        itemRequest.setUserId(1L);
    }

    @Test
    public void testAddItem() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);

        InventoryItemResponse response = inventoryService.addItem(itemRequest);

        assertNotNull(response);
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getQuantity(), response.getQuantity());
        assertEquals(item.getPrice(), response.getPrice());
        assertEquals(item.getCategory().getName(), response.getCategoryName());
        assertEquals(user.getFirstName() + " " + user.getLastName(), response.getUserName());

        verify(categoryRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void testGetAllItems() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        List<InventoryItemResponse> allItems = inventoryService.getAllItems();

        assertNotNull(allItems);
        assertEquals(1, allItems.size());

        InventoryItemResponse response = allItems.get(0);
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getQuantity(), response.getQuantity());
        assertEquals(item.getPrice(), response.getPrice());
        assertEquals(item.getCategory().getName(), response.getCategoryName());
        assertEquals(user.getFirstName() + " " + user.getLastName(), response.getUserName());

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void testGetItemById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        InventoryItemResponse response = inventoryService.getItemById(1L);

        assertNotNull(response);
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getQuantity(), response.getQuantity());
        assertEquals(item.getPrice(), response.getPrice());
        assertEquals(item.getCategory().getName(), response.getCategoryName());
        assertEquals(user.getFirstName() + " " + user.getLastName(), response.getUserName());

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);

        InventoryItemResponse response = inventoryService.updateItem(1L, itemRequest);

        assertNotNull(response);
        assertEquals(itemRequest.getName(), response.getName());
        assertEquals(itemRequest.getQuantity(), response.getQuantity());
        assertEquals(itemRequest.getPrice(), response.getPrice());
        assertEquals(category.getName(), response.getCategoryName());
        assertEquals(user.getFirstName() + " " + user.getLastName(), response.getUserName());

        verify(itemRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteItem() {
        inventoryService.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(anyLong());
    }
}

