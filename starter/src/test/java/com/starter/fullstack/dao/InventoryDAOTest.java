package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test Inventory DAO.
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @ClassRule
  public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";
  private static final String ID = "id";
  private static final String NAME2 = "Tom";
  private static final String PRODUCT_TYPE2 = "toast";
  private static final String NAME3 = "Ren";
  private static final String PRODUCT_TYPE3 = "kite";

  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  // checks if object is saved
  @Test
  public void createTest1() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    Inventory save = this.inventoryDAO.create(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertTrue(actualInventory.contains(save));
  }

  // checks create method when object does not exist
  @Test (expected = IllegalArgumentException.class)
  public void createTest2() {
    Inventory inventory = null;
    Inventory save = this.inventoryDAO.create(inventory);
  }

  // checks if inventory gets a new ID when calling create method
  @Test
  public void createTest3() {
    Inventory inventory1 = new Inventory();
    inventory1.setName(NAME);
    inventory1.setProductType(PRODUCT_TYPE);
    inventory1.setId(ID);

    Inventory save = this.inventoryDAO.create(inventory1);

    Assert.assertNotEquals(ID, save.getId());
  }

  @Test
  public void deleteTest1() {
    Inventory inventory2 = new Inventory();
    inventory2.setName(NAME2);
    inventory2.setProductType(PRODUCT_TYPE2);

    Inventory save2 = this.inventoryDAO.create(inventory2);
    Assert.assertTrue(this.inventoryDAO.findAll().contains(save2));

    Inventory remove = this.inventoryDAO.delete(save2.getId());
    Assert.assertFalse(this.inventoryDAO.findAll().contains(remove));
  }

  // checks when string id is null
  @Test
  public void deleteTest2() {
    try {
      this.inventoryDAO.delete(null);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks when string id is empty
  @Test
  public void deleteTest3() {
    try {
      this.inventoryDAO.delete("");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks if method retrieves the right inventory by comparing id's
  @Test
  public void retrieveTest1() {
    Inventory inventory1 = new Inventory();
    inventory1.setName(NAME2);
    inventory1.setProductType(PRODUCT_TYPE2);
    Inventory saved1 = this.inventoryDAO.create(inventory1);

    Inventory inventory2 = new Inventory();
    inventory2.setName(NAME3);
    inventory2.setProductType(PRODUCT_TYPE3);
    this.inventoryDAO.create(inventory2);

    Inventory retrieved = this.inventoryDAO.retrieve(saved1.getId());
    Assert.assertEquals(retrieved.getId(), saved1.getId());
  }

  // checks when string id is empty
  @Test
  public void retrieveTest2() {
    try {
      this.inventoryDAO.retrieve("");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks when string id is null
  @Test
  public void retrieveTest3() {
    try {
      this.inventoryDAO.retrieve(null);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks if method updates old inventory with new one
  @Test
  public void updateTest1() {
    Inventory newInventory = new Inventory();

    newInventory.setName(NAME2);
    newInventory.setProductType(PRODUCT_TYPE2);
    newInventory = this.inventoryDAO.create(newInventory);
    newInventory.setName("FOOD");
    Inventory updatedInventory = this.inventoryDAO.update(newInventory.getId(), newInventory);
    Assert.assertEquals(updatedInventory.getName(), newInventory.getName());
  }

  // checks when string id is null
  @Test
  public void updateTest2() {
    try {
      Inventory inventory = new Inventory();
      inventory.setName(NAME2);
      inventory.setProductType(PRODUCT_TYPE2);
      this.inventoryDAO.update(null, inventory);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks when string is empty
  @Test
  public void updateTest3() {
    try {
      Inventory inventory = new Inventory();
      inventory.setName(NAME2);
      inventory.setProductType(PRODUCT_TYPE2);

      this.inventoryDAO.update("", inventory);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // checks when inventory is null
  @Test
  public void updateTest4() {
    try {
      Inventory inventory = new Inventory();
      inventory.setName(NAME2);
      inventory.setProductType(PRODUCT_TYPE2);
      this.inventoryDAO.create(inventory);
      this.inventoryDAO.update(inventory.getId(), null);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
