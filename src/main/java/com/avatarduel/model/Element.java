package com.avatarduel.model;

public enum Element {
  WATER("WATER"),
  FIRE("FIRE"),
  AIR("AIR"),
  EARTH("EARTH");

  String name; // and maybe asset file path, buat logo
  private Element(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
};
