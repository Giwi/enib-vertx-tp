package com.enib.cai.vertx.verticle;

import com.enib.cai.vertx.services.Files
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.EventBus;

import javax.inject.Inject;

public class ImagesWorkerVerticle extends AbstractGuiceVerticle {

  @Inject
  private Files files;

  public start() {
    super.start();
    println "deploy images worker verticles"

    EventBus eb = vertx.eventBus

    // Handler beers service
    eb.registerHandler("images.service") { response ->
      try {
        byte[] file = files.get(response.body)
        // Now reply to it
        response.reply(new Buffer(file));
      }catch(Exception exp) {
        byte[] file = files.get("fail.jpg")
        response.reply(new Buffer(file))
      }
    }
  }
}
