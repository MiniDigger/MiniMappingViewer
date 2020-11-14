<template>
  <div class="col-11 offset-1 row">
    <template v-if="toObf">
      <div class="col-3">
        {{ mojangData.returnType }}
      </div>
      <div class="col-3">
        {{ mojangData.mapped }}({{ mojangData.params }})
      </div>
      <div class="col-3" style="color: gray">
        {{ mojangData.obf }}({{ mojangData.params }})
      </div>
      <div class="col-3" v-if="spigotData" style="color: red">
        {{ spigotData.mapped }}({{ spigotData.params }})
        <span v-if="spigotData.warning" style="color: grey">
          {{spigotData.warning}}
        </span>
      </div>
    </template>
    <template v-else>
      <div class="col-3" v-if="spigotData && spigotData.returnType">
        {{ spigotData.returnType }}
      </div>
      <div class="col-3" v-else>
        {{ mojangData.returnType }} (mojang)
      </div>
      <div class="col-3" v-if="spigotData && spigotData.mapped">
        {{ spigotData.mapped }}({{ spigotData.params }})
        <span style="color: gray">(obf {{ mojangData.obf }})</span>
        <span v-if="spigotData.warning" style="color: grey">
          {{spigotData.warning}}
        </span>
      </div>
      <div class="col-3" v-else style="color: gray">
        {{ mojangData.obf }}<span class="arg-color">({{ mojangData.params }})</span>
      </div>
      <div class="col-3" style="color: red">
       {{ mojangData.mapped }}<span class="arg-color">({{ mojangData.params }})</span>
      </div>
    </template>
  </div>
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

<style scoped lang="scss">
  .col-3 {
    overflow-wrap: break-word;
  }

  .arg-color {
    color: black;
  }

  .body--dark {
    .arg-color {
      color: #cacaca;
    }
  }
</style>
