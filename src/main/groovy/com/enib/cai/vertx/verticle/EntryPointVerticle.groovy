package com.enib.cai.vertx.verticle

import org.vertx.groovy.platform.Verticle;


public class EntryPointVerticle extends Verticle {

  public start() {
    println "deploy entry point verticle"

    def config = container.config
    println "config=" + config.toString()

    //deploy worker verticles
    container.deployWorkerVerticle("groovy:com.enib.cai.vertx.verticle.BeersWorkerVerticle", container.config, 4)
    container.deployWorkerVerticle("groovy:com.enib.cai.vertx.verticle.ImagesWorkerVerticle", container.config, 4)

    // deploy Enibar Verticle programaticaly
    container.deployVerticle("groovy:com.enib.cai.vertx.verticle.EnibarVerticle", container.config);
  }

}
