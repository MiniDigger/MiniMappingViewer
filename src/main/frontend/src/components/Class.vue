<template>
  <div class="wrapper pa-2">
    <v-card>
      <v-card-title>{{ item.obfName }} -> {{ item.deobfName }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" lg="5">
            <v-list dense>
              <v-list-item-title>Fields ({{fields.length}}):</v-list-item-title>
              <v-list-item v-for="(field, idx) in fields" :key="idx">
                <Field :field="field" />
              </v-list-item>
            </v-list>
          </v-col>
          <v-col cols="12" lg="7">
            <v-list dense>
              <v-list-item-title>Methods ({{methods.length}}):</v-list-item-title>
              <v-list-item v-for="(method, idx) in methods" :key="idx">
                <Method :method="method" />
              </v-list-item>
            </v-list>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import Field from "@/components/Field";
import Method from "@/components/Method";
export default {
  name: "Class",
  components: {Method, Field},
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  computed: {
    fields() {
      const index = this.item.lines.findIndex(t => t.includes("("));
      if (index !== -1) {
        return this.item.lines.slice(1, index - 1);
      } else {
        return this.item.lines.slice(1, this.item.lines.length)
      }
    },
    methods() {
      const index = this.item.lines.findIndex(t => t.includes("("));
      if (index !== -1) {
        return this.item.lines.slice(index, this.item.lines.length);
      } else {
        return [];
      }
    },
  },
}
</script>

<style scoped>
.v-list-item {
  min-height: 20px;
}
</style>
