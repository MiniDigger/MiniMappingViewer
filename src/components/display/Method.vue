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
      {{ mojangData.returnType }}
      <span v-if="spigotData && spigotData.mapped">
        {{ spigotData.mapped }}({{ mojangData.params }})
        <span style="color: gray">(obf {{ mojangData.obf }})</span>
      </span>
      <span v-else style="color: red">
        {{ mojangData.obf }}<span style="color: black">({{ mojangData.params }})</span>
      </span>
      <span style="color: gray">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ mojangData.mapped }}
      </span>
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
