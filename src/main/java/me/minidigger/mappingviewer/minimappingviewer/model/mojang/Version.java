package me.minidigger.mappingviewer.minimappingviewer.model.mojang;

import java.util.Objects;
import java.util.StringJoiner;

public class Version {

    private final Downloads downloads;

    public Version() {
        downloads = null;
    }

    public Version(Downloads downloads) {
        this.downloads = downloads;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(downloads, version.downloads);
    }

    @Override
    public int hashCode() {
        return Objects.hash(downloads);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Version.class.getSimpleName() + "[", "]")
                .add("downloads=" + downloads)
                .toString();
    }

    public static class Downloads {
        private final Download client;
        private final Download client_mappings;
        private final Download server;
        private final Download server_mappings;

        public Downloads() {
            client = null;
            client_mappings = null;
            server = null;
            server_mappings = null;
        }

        public Downloads(Download client, Download client_mappings, Download server, Download server_mappings) {
            this.client = client;
            this.client_mappings = client_mappings;
            this.server = server;
            this.server_mappings = server_mappings;
        }

        public Download getClient() {
            return client;
        }

        public Download getClient_mappings() {
            return client_mappings;
        }

        public Download getServer() {
            return server;
        }

        public Download getServer_mappings() {
            return server_mappings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Downloads downloads = (Downloads) o;
            return Objects.equals(client, downloads.client) && Objects.equals(client_mappings, downloads.client_mappings) && Objects.equals(server, downloads.server) && Objects.equals(server_mappings, downloads.server_mappings);
        }

        @Override
        public int hashCode() {
            return Objects.hash(client, client_mappings, server, server_mappings);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Downloads.class.getSimpleName() + "[", "]")
                    .add("client=" + client)
                    .add("clientMappings=" + client_mappings)
                    .add("server=" + server)
                    .add("serverMappings=" + server_mappings)
                    .toString();
        }
    }

    public static class Download {
        private final String sha1;
        private final long size;
        private final String url;

        public Download() {
            sha1 = null;
            size = -1;
            url = null;
        }

        public Download(String sha1, long size, String url) {
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
        }

        public String getSha1() {
            return sha1;
        }

        public long getSize() {
            return size;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Download download = (Download) o;
            return size == download.size && Objects.equals(sha1, download.sha1) && Objects.equals(url, download.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sha1, size, url);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Download.class.getSimpleName() + "[", "]")
                    .add("sh1='" + sha1 + "'")
                    .add("size=" + size)
                    .add("url='" + url + "'")
                    .toString();
        }
    }
}
