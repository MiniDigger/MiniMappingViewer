<template>
  <q-page>
    <h1>Mojang Mappings</h1>

    <q-list bordered separator>
      <q-item
        v-for="version in versions"
        :key="version.id"
        :to="'/mojang/' + version.id"
        clickable
        v-ripple
      >
        <q-item-section>{{ version.id }} ({{ version.type }})</q-item-section>
      </q-item>
    </q-list>
  </q-page>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { sendError } from "src/api/notify";

export default {
  name: "MojangMappings",
  computed: {
    ...mapState({
      versionManifest: state => state.mojang.versionManifest
    }),
    versions() {
      if (!this.versionManifest) return [];
      let found114 = false;
      return this.versionManifest.versions.filter(v => {
        if (found114) {
          return false;
        } else {
          if (v.id === "1.14.4") {
            found114 = true;
          }
          return true;
        }
      });
    }
  },
  methods: {
    ...mapActions({
      loadMojangVersions: "mojang/loadVersions"
    })
  },
  mounted() {
    if (this.versionManifest == null) {
      this.loadMojangVersions().catch(error => {
        sendError(error);
      });
    }
  }
};
</script>
