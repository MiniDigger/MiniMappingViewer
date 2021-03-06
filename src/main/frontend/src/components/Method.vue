<template>
  <div>
    {{ obf }} {{ sig }} -> {{ deobf }}
  </div>
</template>

<script>
export default {
  name: "Method",
  props: {
    method: {
      type: String,
      required: true
    }
  },
  computed: {
    split() {
      return this.method.split(" ");
    },
    obf() {
      return this.split[0];
    },
    sig() {
      return this.split[1];
    },
    deobf() {
      return this.split[2];
    }
  }
}
</script>

<style scoped>

</style>
