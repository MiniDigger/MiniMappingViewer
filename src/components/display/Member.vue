<template>
  <div>
    <div v-if="toObf === true && mojangData" style="overflow: auto">
      <q-item-section>
        <q-item-label>
          {{ mojangData.mapped }} -> {{ mojangData.obf }}
          <span v-if="spigotData.mapped" style="color: red">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ spigotData.mapped }}
          </span>
        </q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0">
        Fields ({{ fields.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in fields" :key="idx">
            <Field :mojangData="mojangData.fields[key]" :spigotData="mojangMemberToSpigot(key, false)" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
      <q-item-section v-if="methods.length > 0">
        Methods ({{ methods.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in methods" :key="idx">
            <Method :mojangData="mojangData.methods[key]" :spigotData="mojangMemberToSpigot(key, true)" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
    </div>
    <div v-else-if="toObf === false && mojangData" style="overflow: auto">
      <q-item-section>
        <q-item-label>
          <span v-if="spigotData.mapped">{{ spigotData.mapped }}</span>
          <span v-else>{{ mojangData.obf}}</span>
           -> <span style="color: red">{{ mojangData.obf}}</span>
          <span v-if="mojangData.mapped" style="color: grey">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mapped to {{ mojangData.mapped }}
          </span>
        </q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0">
        Fields ({{ fields.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in fields" :key="idx">
            <Field :mojangData="mojangData.fields[key]" :spigotData="mojangMemberToSpigot(key, false)" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
    </div>
    <q-item-section v-else>
      <q-item-label>Loading... {{ mojangData }}</q-item-label>
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
    mojangMemberToSpigot(key, method) {
      if(!this.spigotData) return null;
      let members = method ? this.spigotData.methods : this.spigotData.fields;
      if(!members) return null;
      let mojang = method ? this.mojangData.methods[key] : this.mojangData.fields[key];
      if(mojang) {
        let result = members[mojang.obf];
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
