package com.starter.fullstack.rest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  private static final String PRODUCT = "PRODUCT2";
  private static final String NAME = "John";
  private static final String PRODUCT2 = "TOAST";
  private static final String NAME2 = "Mark";
  private static final String NEWNAME = "NEWNAME";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setProductType("PRODUCT");
    this.inventory.setName("TEST");
    // Sets the Mongo ID for us
    this.inventory = this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }


  /**
   * Test create endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void create() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setProductType(PRODUCT);
    this.inventory.setName(NAME);

    this.mockMvc.perform(post("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
        .andExpect(status().isOk());
    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }

  @Test
  public void deleteInventory() throws Throwable {
    Inventory inventory2 = new Inventory();
    inventory2.setProductType(PRODUCT2);
    inventory2.setName(NAME2);
    Inventory saved = this.mongoTemplate.save(inventory2);

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());

    List<String> ids = new ArrayList<>();
    ids.add(saved.getId());

    this.mockMvc.perform(post("/inventory/deleteInv")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(ids)))
        .andExpect(status().isOk());

    Assert.assertFalse(this.mongoTemplate.findAll(Inventory.class).contains(saved));
    Assert.assertEquals(1, this.mongoTemplate.findAll(Inventory.class).size());
  }

  @Test
  public void retrieveInventory() throws Throwable {
    Inventory inventory2 = new Inventory();
    inventory2.setProductType(PRODUCT2);
    inventory2.setName(NAME2);
    Inventory saved = this.mongoTemplate.save(inventory2);

    this.mockMvc.perform(get("/inventory/retrieve")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(saved.getId()))
        .andExpect(status().isOk())
        .andExpect(content().json(this.objectMapper.writeValueAsString(saved)));
  }

  @Test
  public void updateInventory() throws Throwable {
    Inventory inventory1 = new Inventory();
    inventory1.setProductType(PRODUCT2);
    inventory1.setName(NAME2);

    Inventory saved = this.mongoTemplate.save(inventory1);
    inventory1.setName(NEWNAME);
    String inventory1ID = saved.getId();


    this.mockMvc.perform(put("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(inventory1)))
        .andExpect(status().isOk());

    Inventory updated1 = this.mongoTemplate.findById(inventory1.getId(), Inventory.class);
    Assert.assertEquals(NEWNAME, updated1.getName());
    Assert.assertEquals(inventory1ID, updated1.getId());


  }

  @Test
  public void findAllInventory() throws Throwable {

    this.mockMvc.perform(get("/inventory")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[" + this.objectMapper.writeValueAsString(this.inventory) + "]"));
  }

}
