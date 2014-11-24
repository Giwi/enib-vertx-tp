package com.enib.cai.vertx.verticle;


import com.enib.cai.vertx.services.Beers
import org.vertx.groovy.core.eventbus.EventBus;

import javax.inject.Inject;

public class BeersWorkerVerticle extends AbstractGuiceVerticle {

  @Inject
  private Beers beers

  @Override
  public start() {
    super.start()
    println "deploy enibar worker verticle"

    EventBus eb = vertx.eventBus

    eb.registerHandler("beers.service") { message ->
      String response = ""
      try {
        switch (message.body) {
          case "beers":
            response = beers.getBeers().encodePrettily()
            break

        //other beers
          default:
            response = beers.getBeer(message.body).encodePrettily();
        }
      } catch (Exception exp) {
        response = "Huston we have a problem"
        exp.printStackTrace();
      }

      // Now reply to it
      message.reply(response)
    }
  }
}
