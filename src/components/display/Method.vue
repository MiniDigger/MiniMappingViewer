<template>
  <span>
    <span v-if="toObf">
      {{ mojangData.returnType }} {{ mojangData.mapped }}({{ mojangData.params }})
      <span style="color: gray">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Obfuscated to {{ mojangData.obf }}
      </span>
      <span v-if="spigotData" style="color: red">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ spigotData.mapped }}
      </span>
    </span>
    <span v-else>
      {{ mojangData.returnType }} {{ mojangData.obf }}({{ mojangData.params }}) ->
      {{ mojangData.mapped }}
    </span>
  </span>
</template>

<script>
export default {
  name: "Method",
  props: {
    mojangData: {
      type: Object,
      required: false
    },
    spigotData: {
      type: Object,
      required: false
    },
    toObf: {
      type: Boolean,
      required: true
    }
  }
};
</script>

<style scoped></style>
