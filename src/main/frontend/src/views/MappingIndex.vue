<template>
  <v-row>
    <v-col cols="12" md="6" offset="3">
      <v-card class="ma-4">
        <v-card-title>
          <h1>Versions for Mapping Provider {{ mappingType }}</h1>
        </v-card-title>
        <v-card-text>
          <VersionList v-if="versions" :versions="versions"/>
          <template v-else>
            Loading...
          </template>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import VersionList from "@/components/VersionList";

export default {
  name: "MappingIndex",
  components: { VersionList },
  computed: {
    ...mapGetters('versions', ['getVersions']),
    mappingType() {
      return this.$route.meta.mappingType;
    },
    versions() {
      return this.getVersions[this.mappingType];
    }
  },
  methods: {
    ...mapActions('versions', ['getVersionsFor'])
  },
  async mounted() {
    if (!this.versions) {
      await this.getVersionsFor(this.mappingType);
    }
  }
};
</script>
