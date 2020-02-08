<template>
  <span>
    <span v-if="toObf"
      >{{ data.dataType }} {{ data.mojang }} -> {{ data.obf }}</span
    >
    <span v-else>{{ data.dataType }} {{ data.obf }} -> {{ data.mojang }}</span>
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
