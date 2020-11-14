<template>
  <MojangMappings :version-id="version" :client="false" :to-obf="true" :query="query"/>
</template>

<script>
import MojangMappings from "components/MojangMappings";
export default {
  name: "MojangServer",
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
