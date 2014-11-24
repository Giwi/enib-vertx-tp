package com.enib.cai.vertx.guice.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import org.vertx.java.core.json.JsonObject;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MongoProvider implements Provider<DB> {

  @Inject
  @Named("mongo")
  private Map<String,Object> config


  private MongoClient mongo;
  private final AtomicBoolean mongoInitialized = new AtomicBoolean(false);

  @Override
  public DB get() {
    if (!mongoInitialized.getAndSet(true)) {
      try {
        mongo = new MongoClient((String)config["host"], (Integer)config["port"]);

        String path = config["staticImgs"];
        staticInit(path);

      } catch (UnknownHostException e) {
        e.printStackTrace();
      }

    }
    return mongo.getDB(config["dbname"]);
  }


  private void staticInit(String path) {
    // Do the static data injection
    // init the beers
    DB db = mongo.getDB(config["dbname"]);
    DBObject bson = ( DBObject ) JSON.parse("{\n" +
        "  \"alcohol\": 6.8,\n" +
        "  \"availability\": \"Year round\",\n" +
        "  \"brewery\": \"Brasserie Affligem (Heineken)\",\n" +
        "  \"description\": \"Affligem Blonde, the classic clear blonde abbey ale, with a gentle roundness and 6.8% alcohol. Low on bitterness, it is eminently drinkable.\",\n" +
        "  \"_id\": \"AffligemBlond\",\n" +
        "  \"img\": \"img/AffligemBlond.jpg\",\n" +
        "  \"label\": \"img/AffligemBlond-label.png\",\n" +
        "  \"name\": \"Affligem Blond\",\n" +
        "  \"serving\": \"Serve in a Snifter\",\n" +
        "  \"style\": \"Belgian-Style Blonde Ale\"\n" +
        "}");
    db.getCollection("beers").save(bson);

    if (path != null) {
      System.out.println("load images path=" + path);
      try {
        //grid fs img
        GridFS gfsImages = new GridFS(db, "img");
        File actual = new File(path);
        for (File f : actual.listFiles()) {
          String fileName = f.getName();
          GridFSInputFile gfsFile = gfsImages.createFile(f);
          gfsFile.setFilename(fileName);
          gfsFile.save();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


}