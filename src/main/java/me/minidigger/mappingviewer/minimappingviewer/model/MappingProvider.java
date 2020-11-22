package me.minidigger.mappingviewer.minimappingviewer.model;

public enum MappingProvider {

    MOJANG("mojang", "Mojang"),
    NOTCH("notch", "Notch"),
    SPIGOT("spigot", "Spigot"),
    YARN("yarn", "Yarn"),
    ;

    private final String first;
    private final String second;

    MappingProvider(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }
}
