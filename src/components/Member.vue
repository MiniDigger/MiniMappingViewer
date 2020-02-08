<template>
  <div>
    <div v-if="toObf">
      <q-item-section>
        <q-item-label>{{ data.mojang }}</q-item-label>
      </q-item-section>
      <q-item-section v-if="fields.length > 0">
        Fields ({{ fields.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in fields" :key="idx">
            <Field :data="data.members[key]" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
      <q-item-section v-if="methods.length > 0">
        Methods ({{ methods.length }}):
        <ul class="q-ma-none">
          <li v-for="(key, idx) in methods" :key="idx">
            <Method :data="data.members[key]" :toObf="toObf" />
          </li>
        </ul>
      </q-item-section>
    </div>
    <q-item-section v-else>
      <q-item-label>{{ data.obf }}</q-item-label>
    </q-item-section>
  </div>
</template>

<script>
import Field from "components/Field";
import Method from "components/Method";
export default {
  name: "Member",
  components: {Method, Field},
  props: {
    data: {
      type: Object,
      required: true
    },
    toObf: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    fields() {
      return Object.keys(this.data.members).filter(key => {
        return this.data.members[key].type === "field";
      });
    },
    methods() {
      return Object.keys(this.data.members).filter(key => {
        return this.data.members[key].type === "method";
      });
    }
  }
};
</script>

<style scoped></style>
