<template>
  <q-page>
    <h1 class="q-ma-sm">Spigot Mappings</h1>

    <div class="row">
      <q-list bordered separator class="col-12">
        <q-item
          v-for="version in versions"
          :key="version.id"
          :to="'/spigot/server/' + version.id"
          clickable
          v-ripple
        >
          <q-item-section>{{ version.id }}</q-item-section>
        </q-item>
      </q-list>
    </div>
  </q-page>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { sendError } from "src/api/notify";

export default {
  name: "SpigotIndex",
  computed: {
    ...mapState({
      versions: state => state.spigot.versions
    })
  },
  methods: {
    ...mapActions({
      loadVersions: "spigot/loadVersions"
    })
  },
  mounted() {
    if (this.versions == null) {
      this.loadVersions().catch(error => {
        sendError(error);
      });
    }
  }
};
</script>
