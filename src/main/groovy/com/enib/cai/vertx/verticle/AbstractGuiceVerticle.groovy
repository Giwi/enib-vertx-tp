package com.enib.cai.vertx.verticle

import com.enib.cai.vertx.guice.GuiceModule
import com.google.inject.Guice
import com.google.inject.Injector
import org.vertx.groovy.platform.Verticle


class AbstractGuiceVerticle extends Verticle {

  @Override
  public start() {
    println("inject dependencies")
    Injector injector = Guice.createInjector(new GuiceModule(container.config))
    injector.injectMembers(this)
  }
}
