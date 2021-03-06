package me.minidigger.mappingviewer.minimappingviewer.model.spigot;

import java.util.Objects;
import java.util.StringJoiner;

public class Info {

    private final String minecraftServer;
    private final String classMappings;
    private final String memberMappings;

    public Info() {
        minecraftServer = null;
        classMappings = null;
        memberMappings = null;
    }

    public Info(String minecraftServer, String classMappings, String memberMappings) {
        this.minecraftServer = minecraftServer;
        this.classMappings = classMappings;
        this.memberMappings = memberMappings;
    }

    public String getMinecraftServer() {
        return minecraftServer;
    }

    public String getClassMappings() {
        return classMappings;
    }

    public String getMemberMappings() {
        return memberMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return Objects.equals(minecraftServer, info.minecraftServer) && Objects.equals(classMappings, info.classMappings) && Objects.equals(memberMappings, info.memberMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minecraftServer, classMappings, memberMappings);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Info.class.getSimpleName() + "[", "]")
                .add("minecraftServer='" + minecraftServer + "'")
                .add("classMappings='" + classMappings + "'")
                .add("memberMappings='" + memberMappings + "'")
                .toString();
    }
}
