<template>
  <div>
    {{ obf }} -> {{ deobf }}
  </div>
</template>

<script>
export default {
  name: "Field",
  props: {
    field: {
      type: String,
      required: true
    }
  },
  computed: {
    split() {
        return this.field.split(" ");
    },
    obf() {
        return this.split[0];
    },
    deobf() {
        return this.split[1];
    }
  }
}
</script>

<style scoped>

</style>
