package com.starter.fullstack.dao;
import com.starter.fullstack.api.Inventory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

/**
 * Inventory DAO
 */
public class InventoryDAO {
  private final MongoTemplate mongoTemplate;
  private static final String NAME = "name";
  private static final String PRODUCT_TYPE = "productType";

  /**
   * Default Constructor.
   * 
   * @param mongoTemplate MongoTemplate.
   */
  public InventoryDAO(MongoTemplate mongoTemplate) {
    Assert.notNull(mongoTemplate, "MongoTemplate must not be null.");
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Constructor to build indexes for rate blackout object
   */
  @PostConstruct
  public void setupIndexes() {
    IndexOperations indexOps = this.mongoTemplate.indexOps(Inventory.class);
    indexOps.ensureIndex(new Index(NAME, Sort.Direction.ASC));
    indexOps.ensureIndex(new Index(PRODUCT_TYPE, Sort.Direction.ASC));
  }

  /**
   * Find All Inventory.
   * 
   * @return List of found Inventory.
   */
  public List<Inventory> findAll() {
    return this.mongoTemplate.findAll(Inventory.class);
  }

  /**
   * Save Inventory.
   * 
   * @param inventory Inventory to Save/Update.
   * @return Created/Updated Inventory.
   */
  public Inventory create(Inventory inventory) {
    Assert.notNull(inventory, "Inventory must not be null.");
    inventory.setId(null);
    return this.mongoTemplate.save(inventory);
  }

  /**
   * Retrieve Inventory.
   * 
   * @param id Inventory id to Retrieve.
   * @return Found Inventory.
   */
  public Optional<Inventory> retrieve(String id) {
    // TODO
    Assert.hasLength(id, "ID must not be empty and not be null");
    Inventory retrieveIn = this.mongoTemplate.findById(id, Inventory.class);
    Optional<Inventory> retrieved = Optional.of(retrieveIn);
    return retrieved;
  }

  /**
   * Update Inventory.
   * 
   * @param id        Inventory id to Update.
   * @param inventory Inventory to Update.
   * @return Updated Inventory.
   */

  public Optional<Inventory> update(String id, Inventory inventory) {
    Assert.hasLength(id, "ID must not be empty and not be null");
    Assert.notNull(inventory, "Inventory must not be null");
    Optional<Inventory> save = retrieve(id);
    // checks to see if object exists to be able to update
    Assert.notNull(save, "Inventory must not be null");
    // updates the old inventory with the new one
    Inventory saveIn = this.mongoTemplate.save(inventory);
    Optional<Inventory> savedInventory = Optional.of(saveIn);
    return savedInventory;
  }

  public List<Optional<Inventory>> delete(List<String> ids) {
    List<Optional<Inventory>> retList = new ArrayList<>();

    for (String id : ids) {
      Assert.hasLength(id, "ID must not be empty and not be null");
      Query query = new Query();
      query.addCriteria(Criteria.where("id").is(id));
      Inventory removeIn = (mongoTemplate.findAndRemove(query, Inventory.class));
      Optional<Inventory> removedInventory = Optional.of(removeIn);
      retList.add(removedInventory);
    }
    return retList;
  }
}
