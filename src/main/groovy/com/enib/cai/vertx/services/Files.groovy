package com.enib.cai.vertx.services;

public interface Files {

  public byte[] get(String name);

  public void put(byte[] data, String name);
}
