<template>
  <v-row>
    <v-col cols="12" :md="hasSnapshots ? 6 : 12">
      <v-list dense>
        <v-list-item v-for="v in firstList" :key="v.id" :to="$route.path + '/' + v.id">
          <v-chip
            :color="v.type === 'release' ? 'primary' : 'secondary'"
            :text-color="v.type === 'release' ? 'white' : 'black'">
            {{ v.type }}
          </v-chip>
          <span class="ml-3">{{ v.id }}</span>
        </v-list-item>
      </v-list>
    </v-col>
    <v-col v-if="hasSnapshots" cols="12" :md="6">
      <v-list dense>
        <v-list-item v-for="v in secondList" :key="v.id" :to="$route.path + '/' + v.id">
          <v-chip
            :color="v.type === 'release' ? 'primary' : 'secondary'"
            :text-color="v.type === 'release' ? 'white' : 'black'">
            {{ v.type }}
          </v-chip>
          <span class="ml-3">{{ v.id }}</span>
        </v-list-item>
      </v-list>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "MappingIndex",
  props: {
    versions: {
      type: Array,
      required: true
    }
  },
  computed: {
    hasSnapshots() {
      return this.versions.find(v => v.type === "snapshot");
    },
    firstList() {
      return this.hasSnapshots ? this.versions.filter(v => v.type === "release") : this.versions;
    },
    secondList() {
      return this.hasSnapshots ? this.versions.filter(v => v.type === "snapshot") : [];
    }
  }
};
</script>
