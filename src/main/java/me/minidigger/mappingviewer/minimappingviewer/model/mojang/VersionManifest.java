package me.minidigger.mappingviewer.minimappingviewer.model.mojang;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class VersionManifest {

    private final Latest latest;
    private final List<Version> versions;

    public VersionManifest() {
        latest = null;
        versions = null;
    }

    public VersionManifest(Latest latest, List<Version> versions) {
        this.latest = latest;
        this.versions = versions;
    }

    public Latest getLatest() {
        return latest;
    }

    public List<Version> getVersions() {
        return versions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionManifest that = (VersionManifest) o;
        return Objects.equals(latest, that.latest) && Objects.equals(versions, that.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latest, versions);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VersionManifest.class.getSimpleName() + "[", "]")
                .add("latest=" + latest)
                .add("versions=" + versions)
                .toString();
    }

    public static class Latest {
        private final String release;
        private final String snapshot;

        public Latest() {
            release = null;
            snapshot = null;
        }

        public Latest(String release, String snapshot) {
            this.release = release;
            this.snapshot = snapshot;
        }

        public String getRelease() {
            return release;
        }

        public String getSnapshot() {
            return snapshot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Latest latest = (Latest) o;
            return Objects.equals(release, latest.release) && Objects.equals(snapshot, latest.snapshot);
        }

        @Override
        public int hashCode() {
            return Objects.hash(release, snapshot);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Latest.class.getSimpleName() + "[", "]")
                    .add("release='" + release + "'")
                    .add("snapshot='" + snapshot + "'")
                    .toString();
        }
    }

    public static class Version {
        private final String id;
        private final String type;
        private final String url;

        public Version() {
            id = null;
            type = null;
            url = null;
        }

        public Version(String id, String type, String url) {
            this.id = id;
            this.type = type;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Version version = (Version) o;
            return Objects.equals(id, version.id) && Objects.equals(type, version.type) && Objects.equals(url, version.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, type, url);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Version.class.getSimpleName() + "[", "]")
                    .add("id='" + id + "'")
                    .add("tpye='" + type + "'")
                    .add("url='" + url + "'")
                    .toString();
        }
    }
}
