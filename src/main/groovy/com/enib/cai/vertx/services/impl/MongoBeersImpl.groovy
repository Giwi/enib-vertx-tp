package com.enib.cai.vertx.services.impl;

import com.enib.cai.vertx.model.Beer;
import com.enib.cai.vertx.services.Beers;
import com.mongodb.*;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import javax.inject.Inject;

public class MongoBeersImpl implements Beers {

  // MongoDB driver
  @Inject
  private DB db;

  public MongoBeersImpl() {
  }
  
  
  
  public JsonArray getBeers() {
    JsonArray jsonBeers = new JsonArray();
    DBCursor cursor = null;
    try {
      // GET ALL BEERS
      DBCollection beersCollection = db.getCollection("beers");

      cursor = beersCollection.find();

      while (cursor.hasNext()) {
        DBObject object = cursor.next();

        JsonObject beer = new JsonObject();

        beer.putNumber("alcohol", (Number) object.get("alcohol"));
        beer.putString("description", (String) object.get("description"));
        beer.putString("id",(String) object.get("_id"));
        beer.putString("img",(String) object.get("img"));
        beer.putString("name",(String) object.get("name"));

        jsonBeers.addObject(beer);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return jsonBeers;
  }


  @Override
  public JsonObject getBeer(String id) throws Exception {
    JsonObject beer = new JsonObject();
    DBCursor cursor = null;
    try {
      // GET ALL BEERS
      DBCollection beersCollection = db.getCollection("beers");

      DBObject query = new BasicDBObject("_id", id);
      cursor = beersCollection.find(query);

      switch (cursor.size()) {
        case 0:
          throw new Exception("Unknown id");
        case 1:
          //wrap the response
          DBObject dbObject = cursor.next();

          beer.putNumber("alcohol", (Number) dbObject.get("alcohol"));
          beer.putString("description", (String) dbObject.get("description"));
          beer.putString("id", (String) dbObject.get("_id"));
          beer.putString("img", (String) dbObject.get("img"));
          beer.putString("name", (String) dbObject.get("name"));
          beer.putString("availability", (String) dbObject.get("availability"));
          beer.putString("brewery", (String) dbObject.get("brewery"));
          beer.putString("label", (String) dbObject.get("label"));
          beer.putString("serving", (String) dbObject.get("serving"));
          beer.putString("style", (String) dbObject.get("style"));

          break;
        default:
          throw new Exception("Ouch! More than one result was found");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return beer;
  }

  @Override
  public JsonObject addBeer(Beer beer) throws Exception {
    try {
      DBCollection beersCollection = db.getCollection("beers");

      DBObject dbBeer = new BasicDBObject();

      dbBeer.put("alcohol", beer.alcohol);
      dbBeer.put("description", beer.description);
      dbBeer.put("_id", beer.id);
      dbBeer.put("img", beer.img);
      dbBeer.put("name", beer.name);
      dbBeer.put("availability", beer.availability);
      dbBeer.put("brewery", beer.brewery);
      dbBeer.put("label", beer.label);
      dbBeer.put("serving", beer.serving);
      dbBeer.put("style", beer.style);

      beersCollection.insert(dbBeer).getLastError().throwOnError();

      return getBeer(beer.id);
    } catch(com.mongodb.MongoException.DuplicateKey exp) {
      throw new Exception("already exists");
    } catch (Exception e) {
      throw e;
    }
  }
}
