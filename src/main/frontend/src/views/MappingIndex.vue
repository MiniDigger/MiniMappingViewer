<template>
  <div class="mapping-index">
    <h1>Mapping Index for {{ mappingType }}</h1>
  </div>
</template>

<script>
export default {
  name: "MappingIndex",
  computed: {
    mappingType() {
      return this.$route.meta.mappingType;
    }
  }
};
</script>
