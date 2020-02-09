<template>
  <div class="col-11 offset-1 row">
    <template v-if="toObf">
      <div class="col-3">
        {{ mojangData.dataType }}
      </div>
      <div class="col-3">
        {{ mojangData.mapped }}
      </div>
      <div class="col-3" style="color: gray">
        {{ mojangData.obf }}
      </div>
      <div class="col-3" v-if="spigotData" style="color: red">
        {{ spigotData.mapped }}
      </div>
    </template>
    <template v-else>
      <div class="col-3">
        {{ mojangData.dataType }}
      </div>
      <div class="col-3" v-if="spigotData && spigotData.mapped">
        {{ spigotData.mapped }} ({{ mojangData.obf }})
      </div>
      <div class="col-3" v-else style="color: gray">
        {{ mojangData.obf }}
      </div>
      <div class="col-3" style="color: red">
        {{ mojangData.mapped }}
      </div>
    </template>
  </div>
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

<style scoped>
  .col-3 {
    overflow-wrap: break-word;
  }
</style>
