package com.enib.cai.vertx.verticle

import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.groovy.platform.Verticle;


public class EnibarVerticle extends Verticle {

  public start() {
    println "deploy enibar verticle"

    EventBus eb = vertx.eventBus
    eb.javaEventBus().setDefaultReplyTimeout(5000)

    println "Launch the verticle routeMatcher"

    RouteMatcher routeMatcher = new RouteMatcher()

    // API Route Matcher send to the event bus
    routeMatcher.get("/api/beers/:id") { req ->
      String id = req.params.get("id")
      // send the message throw the eventbus
      eb.send("beers.service", id) { response ->
        // get the response from the eventbus and send it as response
        println("I received a reply before the timeout of 5 seconds");
        req.response.end(response.body)
      }
    }


    // API Route Matcher send to the event bus
    routeMatcher.get("/img/:name") { req  ->
      String filename = req.get("name")

      eb.send("images.service", filename) { response ->
        req.response.putHeader("Content-Length", Integer.toString(response.body.length))
        req.response << response.body()
        req.response.end();
      }
    }

    // otherwise  send file
    routeMatcher.noMatch() { req ->
      String file = "";
      if (req.path.equals("/")) {
        file = "index.html";
      } else if (!req.path.contains("..")) {
        file = req.path();
      }
      req.response.sendFile("web/" + file);
    }

    vertx.createHttpServer().requestHandler(routeMatcher.asClosure()).listen(44081)
  }
}
