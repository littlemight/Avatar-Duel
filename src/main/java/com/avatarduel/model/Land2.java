package com.avatarduel.model;

public class Land2 {
  private String name;
  private String description;
  private Element element;

  public Land2() {
    this.name = "";
    this.description = "";
    this.element = Element.AIR;
  }

  public Land2(String name, String description, Element element) {
    this.name = name;
    this.description = description;
    this.element = element;
  }
}