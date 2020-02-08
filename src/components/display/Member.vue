<template>
  <div>
    <div v-if="toObf && mojangData" style="overflow: auto">
      <q-item-section>
        <q-item-label>{{ mojangData.mapped }} -> {{ mojangData.obf }}</q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0">
        Fields ({{ fields.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in fields" :key="idx">
            <Field :mojangData="mojangData.fields[key]" :spigotData="getSpigotField(key)" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
      <q-item-section v-if="methods.length > 0">
        Methods ({{ methods.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in methods" :key="idx">
            <Method :mojangData="mojangData.methods[key]" :spigotData="getSpigotMethod(key)" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
    </div>
    <q-item-section v-else>
      <q-item-label>{{ mojangData }}</q-item-label>
    </q-item-section>
  </div>
</template>

<script>
import Field from "components/display/Field";
import Method from "components/display/Method";
export default {
  name: "Member",
  components: { Method, Field },
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
  },
  computed: {
    mojangKeys() {
      return Object.keys(
        this.mojangData && this.mojangData.members ? this.mojangData.members : {}
      )
    },
    fields() {
      return Object.keys(this.mojangData.fields);
    },
    methods() {
      return Object.keys(this.mojangData.methods);
    }
  },
  methods: {
    getSpigotField(key) {
      if(!this.spigotData || !this.spigotData.fields) return null;
      let mojang = this.mojangData.fields[key];
      if(mojang) {
        let result = this.spigotData.fields[mojang.obf];
        if(result instanceof Function) {
          return  null;
        } else {
          return result;
        }
      } else {
        console.log("meh " + key);
        return null;
      }
    },
    getSpigotMethod(key) {
      if(!this.spigotData || !this.spigotData.methods) return null;
      let mojang = this.mojangData.methods[key];
      if(mojang) {
        let result = this.spigotData.methods[mojang.obf];
        if(result instanceof Function) {
          return  null;
        } else {
          return result;
        }
      } else {
        console.log("meh " + key);
        return null;
      }
    }
  }
};
</script>

<style scoped></style>
