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

//  @Test
//  public void deleteTest1() {
//    Inventory inventory = new Inventory();
//    inventory.setName(NAME);
//    inventory.setProductType(PRODUCT_TYPE);
//
//    Inventory save1 = this.inventoryDAO.create(inventory);
//    Assert.assertEquals(1,this.inventoryDAO.findAll().size());
//
//    Inventory remove = this.inventoryDAO.remove(save);
//    Assert.assertEquals(0, this.inventoryDAO.findAll().size());
//  }



}
