package org.bahadircolak.inventorymanagement.web;

import org.bahadircolak.inventorymanagement.service.InventoryService;
import org.bahadircolak.inventorymanagement.service.UserService;
import org.bahadircolak.inventorymanagement.web.dto.UserDto;
import org.bahadircolak.inventorymanagement.web.request.AuthenticationRequest;
import org.bahadircolak.inventorymanagement.web.request.RegisterRequest;
import org.bahadircolak.inventorymanagement.web.request.UpdateUserRequest;
import org.bahadircolak.inventorymanagement.web.response.AuthenticationResponse;
import org.bahadircolak.inventorymanagement.web.response.InventoryItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final InventoryService inventoryService;

    public UserController(UserService userService, InventoryService inventoryService) {
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/inventory")
    public ResponseEntity<List<InventoryItemResponse>> getUserInventory(@PathVariable Long id) {
        List<InventoryItemResponse> inventory = userService.getUserInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/inventory/total-value")
    public ResponseEntity<Double> getTotalInventoryValue(@PathVariable Long id) {
        double totalValue = userService.calculateTotalInventoryValue(id);
        return ResponseEntity.ok(totalValue);
    }

    @PutMapping("/{id}/inventory/apply-discount")
    public ResponseEntity<Void> applyDiscountToInventory(@PathVariable Long id, @RequestParam double discountPercentage) {
        inventoryService.applyDiscount(id, discountPercentage);
        return ResponseEntity.noContent().build();
    }
}