<template>
  <span>
    <span v-if="toObf"
      >{{ data.dataType }} {{ data.mapped }} -> {{ data.obf }}</span
    >
    <span v-else>{{ data.dataType }} {{ data.obf }} -> {{ data.mapped }}</span>
  </span>
</template>

<script>
export default {
  name: "Field",
  props: {
    data: {
      type: Object,
      required: true
    },
    toObf: {
      type: Boolean,
      required: true
    }
  }
};
</script>

<style scoped></style>
