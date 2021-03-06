package me.minidigger.mappingviewer.minimappingviewer.model;

public enum MappingProvider {

    MOJANG("mojang", "Mojang", true),
    NOTCH("notch", "Notch", true),
    SPIGOT("spigot", "Spigot", false),
    YARN("yarn", "Yarn", true),
    ;

    private final String first;
    private final String second;
    private final boolean snapshots;

    MappingProvider(String first, String second, boolean snapshots) {
        this.first = first;
        this.second = second;
        this.snapshots = snapshots;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public boolean supportsSnapshots() {
        return snapshots;
    }
}
