package com.enib.cai.vertx.verticle;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class EntryPointVerticle extends Verticle {

  public void start() {
    System.out.println("deploy entry point verticle");

    JsonObject config = container.config();
    System.out.println("config=" + config.toString());

    //deploy worker verticles
    container.deployWorkerVerticle("com.enib.cai.vertx.verticle.BeersWorkerVerticle", container.config(), 4);
    container.deployWorkerVerticle("com.enib.cai.vertx.verticle.ImagesWorkerVerticle", container.config(), 4);

    // deploy Enibar Verticle programaticaly
    container.deployVerticle("com.enib.cai.vertx.verticle.EnibarVerticle", container.config());



  }

}
