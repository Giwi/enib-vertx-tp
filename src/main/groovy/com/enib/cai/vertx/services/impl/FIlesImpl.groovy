package com.enib.cai.vertx.services.impl;

import com.enib.cai.vertx.services.Files;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import javax.inject.Inject;

public class FIlesImpl implements Files {
  @Inject
  private DB db;

  public byte[] get(String name) {
    try {
      GridFS gfsPhoto = new GridFS(db, "img");
      GridFSDBFile imageForOutput = gfsPhoto.findOne(name);

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      imageForOutput.writeTo(bos);
      return bos.toByteArray();
    } catch( Exception exp) {

    }
    return null;
  }

  @Override
  public void put(byte[] data, String name) {
    GridFS gfsImages = new GridFS(db, "img");
    GridFSInputFile gfsFile = gfsImages.createFile(data);
    gfsFile.setFilename(name);
    gfsFile.save();
  }
}
