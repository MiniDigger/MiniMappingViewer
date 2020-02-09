<template>
  <span>
    <span v-if="toObf">
      {{ mojangData.dataType }} {{ mojangData.mapped }}
      <span style="color: gray">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Obfuscated to {{ mojangData.obf }}
      </span>
      <span v-if="spigotData" style="color: red">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ spigotData.mapped }}
      </span>
    </span>
    <span v-else>
      {{ mojangData.dataType }}
      <span v-if="spigotData && spigotData.mapped">
        {{ spigotData.mapped }} ({{ mojangData.obf }})
      </span>
      <span v-else style="color: red">
        {{ mojangData.obf }}
      </span>
      <span style="color: gray">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ mojangData.mapped }}
      </span>
    </span>
  </span>
</template>

<script>
export default {
  name: "Field",
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
