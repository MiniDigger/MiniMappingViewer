<template>
  <MojangMappings :version-id="version" :client="true" />
</template>

<script>
import MojangMappings from "components/MojangMappings";
export default {
  name: "MojangClient",
  components: { MojangMappings },
  computed: {
    version() {
      return this.$route.params.versionId;
    }
  }
};
</script>

<style scoped></style>
