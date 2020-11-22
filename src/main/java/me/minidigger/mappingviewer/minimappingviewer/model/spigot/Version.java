package me.minidigger.mappingviewer.minimappingviewer.model.spigot;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class Version {

    private final String name;
    private final Refs refs;

    public Version() {
        refs = null;
        name = null;
    }

    public Version(Refs refs, String name) {
        this.refs = refs;
        this.name = name;
    }

    public Refs getRefs() {
        return refs;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(refs, version.refs) && Objects.equals(name, version.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refs, name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Version.class.getSimpleName() + "[", "]")
                .add("refs=" + refs)
                .add("name='" + name + "'")
                .toString();
    }

    public static class Refs {

        @JsonProperty("BuildData")
        private final String BuildData;
        @JsonProperty("Bukkit")
        private final String Bukkit;
        @JsonProperty("CraftBukkit")
        private final String CraftBukkit;
        @JsonProperty("Spigot")
        private final String Spigot;

        public Refs() {
            BuildData = null;
            Bukkit = null;
            CraftBukkit = null;
            Spigot = null;
        }

        public Refs(String BuildData, String Bukkit, String CraftBukkit, String Spigot) {
            this.BuildData = BuildData;
            this.Bukkit = Bukkit;
            this.CraftBukkit = CraftBukkit;
            this.Spigot = Spigot;
        }

        public String getBuildData() {
            return BuildData;
        }

        public String getBukkit() {
            return Bukkit;
        }

        public String getCraftBukkit() {
            return CraftBukkit;
        }

        public String getSpigot() {
            return Spigot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Refs refs = (Refs) o;
            return Objects.equals(BuildData, refs.BuildData) && Objects.equals(Bukkit, refs.Bukkit) && Objects.equals(CraftBukkit, refs.CraftBukkit) && Objects.equals(Spigot, refs.Spigot);
        }

        @Override
        public int hashCode() {
            return Objects.hash(BuildData, Bukkit, CraftBukkit, Spigot);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Refs.class.getSimpleName() + "[", "]")
                    .add("BuildData='" + BuildData + "'")
                    .add("Bukkit='" + Bukkit + "'")
                    .add("CraftBukkit='" + CraftBukkit + "'")
                    .add("Spigot='" + Spigot + "'")
                    .toString();
        }
    }
}
