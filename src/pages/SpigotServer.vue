<template>
  <MojangMappings :version-id="version" :client="false" :to-obf="false" :query="query"/>
</template>

<script>
import MojangMappings from "components/MojangMappings";
export default {
  name: "SpigotServer",
  components: { MojangMappings },
  computed: {
    version() {
      return this.$route.params.versionId;
    },
    query() {
      return this.$route.params.query;
    }
  }
};
</script>

<style scoped></style>
