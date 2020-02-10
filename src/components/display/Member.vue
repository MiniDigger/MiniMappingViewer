<template>
  <div>
    <div v-if="toObf === true && mojangData" style="overflow: auto">
      <q-item-section>
        <q-item-label class="text-subtitle1" style="background-color: #d3d3d370">
          <div class="row">
            <div class="col-1">Class:</div>
            <div class="col-11 row">
              <div class="offset-3 col-3">{{ mojangData.mapped }}</div>
              <div class="col-3" style="color: grey">{{ mojangData.obf }}</div>
              <div class="col-3" v-if="spigotData && spigotData.mapped" style="color: red">
                {{ spigotData.mapped }}
              </div>
            </div>
          </div>
        </q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0">
        <span class="text-subtitle2">Fields ({{ fields.length }}):</span>
        <div class="row">
          <div class="col-11 offset-1 row">
            <div class="col-3">Data Type</div>
            <div class="col-3">Mojang Name</div>
            <div class="col-3">Obf Name</div>
            <div class="col-3">Spigot Name</div>
          </div>
          <q-separator class="col-11 offset-1"/>
          <Field v-for="(key, idx) in fields" :key="idx" :mojangData="mojangData.fields[key]"
                 :spigotData="mojangMemberToSpigot(key, false)" :toObf="toObf"/>
        </div>
      </q-item-section>
      <q-item-section v-if="methods.length > 0">
        <span class="text-subtitle2">Methods ({{ methods.length }}):</span>
        <div class="row">
          <div class="col-11 offset-1 row">
            <div class="col-3">Return Type</div>
            <div class="col-3">Mojang Name</div>
            <div class="col-3">Obf Name</div>
            <div class="col-3">Spigot Name</div>
          </div>
          <q-separator class="col-11 offset-1"/>
          <Method v-for="(key, idx) in methods" :key="idx" :mojangData="mojangData.methods[key]"
                  :spigotData="mojangMemberToSpigot(key, true)" :toObf="toObf"/>
        </div>
      </q-item-section>
    </div>

    <div v-else-if="toObf === false && mojangData" style="overflow: auto">
      <q-item-section>
        <q-item-label class="text-subtitle1" style="background-color: #d3d3d370">
          <div class="row">
            <div class="col-1">Class:</div>
            <div class="col-11 row">
              <div class="col-3" v-if="spigotData && spigotData.mapped">{{ spigotData.mapped }}</div>
              <div class="col-3" v-else>{{ mojangData.obf}}</div>
              <div class="col-3" style="color: grey">{{ mojangData.obf}}</div>
              <div class="col-3" v-if="mojangData.mapped" style="color: red">
                {{ mojangData.mapped }}
              </div>
            </div>
          </div>
        </q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0" class="q-ma-none">
        <span class="text-subtitle2">Fields ({{ fields.length }}):</span>
        <div class="row">
          <div class="col-11 offset-1 row">
            <div class="col-3">Data Type</div>
            <div class="col-3">Spigot Name (gray for obf)</div>
            <div class="col-3">Mojang Name</div>
          </div>
          <q-separator class="col-11 offset-1"/>
          <Field v-for="(key, idx) in fields" :key="idx" :mojangData="mojangData.fields[key]"
                 :spigotData="mojangMemberToSpigot(key, false)" :toObf="toObf"/>
        </div>
      </q-item-section>
      <q-item-section v-if="methods.length > 0">
        <span class="text-subtitle2">Methods ({{ methods.length }}):</span>
        <div class="row">
          <div class="col-11 offset-1 row">
            <div class="col-3">Return Type</div>
            <div class="col-3">Spigot Name (gray for obf)</div>
            <div class="col-3">Mojang Name</div>
          </div>
          <q-separator class="col-11 offset-1"/>
          <Method v-for="(key, idx) in methods" :key="idx" :mojangData="mojangData.methods[key]"
                  :spigotData="mojangMemberToSpigot(key, true)" :toObf="toObf"/>
        </div>
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
    components: {Method, Field},
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
        if (!this.spigotData) return null;
        let members = method ? this.spigotData.methods : this.spigotData.fields;
        if (!members) return null;
        let mojang = method ? this.mojangData.methods[key] : this.mojangData.fields[key];
        if (mojang) {
          let candidates = Object.keys(members).filter(key => key.startsWith(mojang.obf));
          if (candidates && candidates.length > 0) {
            // fix shit
            if(!mojang.params) mojang.params = "";
            candidates.forEach(key => {
              if(!members[key].params) {
                members[key].params = "";
              }
            });
            // get mojang param count
            let mojangParamCount = mojang.params.split(",").length === 1 ? (mojang.params.length > 0 ? 1 : 0) : mojang.params.split(",").length;
            // find who matches that count
            candidates = candidates.filter(key => mojangParamCount === (members[key].params.split(",").length === 1 ? (members[key].params.length > 0 ? 1 : 0) : members[key].params.split(",").length));

            if (candidates && candidates.length > 0) {
              if (candidates.length === 1) {
                if(mojangParamCount !== 0) members[candidates[0]].warning = "maybe wrong";
                return members[candidates[0]]; // todo this is wrong, might have wrong params
              } else {
                // console.log("found too many..." + key);
                members[candidates[1]].warning = "most likely wrong";
                return members[candidates[1]]; // TODO this is wrong, we need to find the right one, based on params
              }
            } else {
              // console.log("found nothing1! " + key);
              return null;
            }
          } else {
            // console.log("found nothing2! " + key);
            return null;
          }
        } else {
          // console.log("meh " + key);
          return null;
        }
      }
    }
  };
</script>

<style scoped lang="scss">
  .col-11.offset-1.row:nth-child(even) {
    background-color: #d3d3d370;
  }
</style>
