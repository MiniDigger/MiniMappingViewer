<template>
  <div class="mapping-view">
    <h1>Mapping View for {{ mappingType }} on {{ version }}</h1>
  </div>
</template>

<script>
export default {
  name: "MappingView",
  computed: {
     mappingType() {
         return this.$route.meta.mappingType;
     },
     version() {
         return this.$route.params.version;
     }
  }
};
</script>
