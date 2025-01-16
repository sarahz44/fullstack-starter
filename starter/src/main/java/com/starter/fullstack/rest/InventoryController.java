package com.starter.fullstack.rest;

import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.dao.InventoryDAO;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Inventory Controller.
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
  private final InventoryDAO inventoryDAO;

  /**
   * Default Constructor.
   * @param inventoryDAO inventoryDAO.
   */
  public InventoryController(InventoryDAO inventoryDAO) {
    Assert.notNull(inventoryDAO, "Inventory DAO must not be null.");
    this.inventoryDAO = inventoryDAO;
  }

  /**
   * Find Products.
   * @return List of Product.
   */
  @GetMapping
  public List<Inventory> findInventories() {
    return this.inventoryDAO.findAll();
  }

  @PostMapping
  public Inventory createInventory(@Valid @RequestBody Inventory inventory) {
    return this.inventoryDAO.create(inventory);
  }

  @DeleteMapping
  public Optional<Inventory> deleteInventory(@Valid @RequestBody String id) {
    return this.inventoryDAO.delete(id);
  }

  @GetMapping("/retrieve")
  public Optional<Inventory> retrieveInventory(@Valid @RequestBody String id) {
    return this.inventoryDAO.retrieve(id);
  }

  @PutMapping
  public Optional<Inventory> updateInventory(@Valid @RequestBody Inventory inventory) {
    String id = inventory.getId();
    return this.inventoryDAO.update(id, inventory);
  }

}

