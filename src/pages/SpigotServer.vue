<template>
  <MojangMappings :version-id="version" :client="false" :to-obf="false"/>
</template>

<script>
import MojangMappings from "components/MojangMappings";
export default {
  name: "SpigotServer",
  components: { MojangMappings },
  computed: {
    version() {
      return this.$route.params.versionId;
    }
  }
};
</script>

<style scoped></style>
