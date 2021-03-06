<template>
  <v-row>
    <v-col cols="12" lg="6" offset-lg="3">
      <v-card class="ma-4">
        <v-card-title>
          <h1>Versions for {{ title }}</h1>
        </v-card-title>
        <v-card-text>
          <VersionList v-if="versions" :versions="versions"/>
          <Loader v-else />
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import VersionList from "@/components/VersionList";
import Loader from "@/components/Loader";

export default {
  name: "MappingIndex",
  components: {Loader, VersionList },
  computed: {
    ...mapGetters('versions', ['getVersions']),
    title() {
      return this.$route.name
    },
    versions() {
      if (this.$route.meta.merge) {
        const left = this.getVersions[this.$route.meta.leftType];
        const right = this.getVersions[this.$route.meta.rightType];
        if (!left || !right) {
          return null;
        }
        const combined = [];
        left.forEach(l => {
          right.forEach(r => {
            if (l.id === r.id) {
              combined.push(l);
            }
          })
        });
        return combined;
      } else {
        return this.getVersions[this.$route.meta.mappingType];
      }
    }
  },
  methods: {
    ...mapActions('versions', ['getVersionsFor'])
  },
  async mounted() {
    if (!this.versions) {
      if (this.$route.meta.merge) {
        await this.getVersionsFor(this.$route.meta.leftType);
        await this.getVersionsFor(this.$route.meta.rightType);
      } else {
        await this.getVersionsFor(this.$route.meta.mappingType);
      }
    }
  }
};
</script>
