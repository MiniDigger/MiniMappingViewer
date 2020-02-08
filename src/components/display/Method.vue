<template>
  <span>
    <span v-if="toObf">
      {{ data.returnType }} {{ data.mojang }}({{ data.params }}) ->
      {{ data.obf }}
    </span>
    <span v-else>
      {{ data.returnType }} {{ data.obf }}({{ data.params }}) ->
      {{ data.mojang }}
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
