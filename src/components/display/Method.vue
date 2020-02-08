<template>
  <span>
    <span v-if="toObf">
      {{ data.returnType }} {{ data.mapped }}({{ data.params }}) ->
      {{ data.obf }}
    </span>
    <span v-else>
      {{ data.returnType }} {{ data.obf }}({{ data.params }}) ->
      {{ data.mapped }}
    </span>
  </span>
</template>

<script>
export default {
  name: "Method",
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
