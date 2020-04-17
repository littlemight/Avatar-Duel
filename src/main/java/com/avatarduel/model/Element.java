package com.avatarduel.model;

public enum Element {
  WATER("card/image/element/water.png"),
  FIRE("card/image/element/fire.png"),
  AIR("card/image/element/air.png"),
  EARTH("card/image/element/earth.png"),
  ENERGY("card/image/element/water.png");

  String path; // or maybe asset file path, buat logo
  private Element(String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return path;
  }
};
