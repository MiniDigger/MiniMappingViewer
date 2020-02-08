<template>
  <q-page>
    <h1 class="q-ma-sm">Mojang Mappings</h1>

    <div class="row">
      <div class="col-6">
        <h3 class="q-ma-sm">Client</h3>
      </div>
      <div class="col-6">
        <h3 class="q-ma-sm">Server</h3>
      </div>
      <q-list bordered separator class="col-6">
        <q-item
          v-for="version in versions"
          :key="version.id"
          :to="'/mojang/client/' + version.id"
          clickable
          v-ripple
        >
          <q-item-section>{{ version.id }} ({{ version.type }})</q-item-section>
        </q-item>
      </q-list>
      <q-list bordered separator class="col-6">
        <q-item
          v-for="version in versions"
          :key="version.id"
          :to="'/mojang/server/' + version.id"
          clickable
          v-ripple
        >
          <q-item-section>{{ version.id }} ({{ version.type }})</q-item-section>
        </q-item>
      </q-list>
    </div>
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
