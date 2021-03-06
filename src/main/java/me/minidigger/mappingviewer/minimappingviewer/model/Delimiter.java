package me.minidigger.mappingviewer.minimappingviewer.model;

public enum Delimiter {

  NEWLINE("\n"),
  SPACE(" "),
  NOTHING(""),
  SEMICOLON(";")
  ;


  private String delimiter;

  Delimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public String getDelimiter() {
    return delimiter;
  }
}
