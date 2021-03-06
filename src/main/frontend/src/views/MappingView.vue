<template>
  <v-row>
    <v-col cols="12">
      <v-card class="ma-4">
        <v-card-title>
          <h1>Mapping View for {{ title }} on {{ version }}</h1>
        </v-card-title>
        <v-card-text>

          <DynamicScroller
            v-if="classes.length > 0"
            :items="classes"
            :min-item-size="70"
            class="scroller"
            key-field="deobfName"
            pageMode
          >
            <template v-slot="{ item, index, active }">
              <DynamicScrollerItem
                :item="item"
                :active="active"
                :size-dependencies="[item.lines]"
                :data-index="index"
              >
                <Class :item="item"/>
              </DynamicScrollerItem>
            </template>
          </DynamicScroller>
          <Loader v-else/>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import Class from "@/components/Class";
import Loader from "@/components/Loader";

export default {
  name: "MappingView",
  components: {Loader, Class},
  computed: {
    ...mapGetters('mappings', ['getMappings']),
    ...mapGetters('merge', ['getMergedMappings']),
    title() {
      return this.$route.name.slice(0, -1);
    },
    version() {
      return this.$route.params.version;
    },
    mappings() {
      if (this.$route.meta.merge) {
        return this.getMergedMappings[this.$route.meta.leftType + "-" + this.$route.meta.rightType + "-" + this.version];
      } else {
        return this.getMappings[this.$route.meta.mappingType + "-" + this.version];
      }
    },
    classes() {
      if (!this.mappings) return [];
      const arr = [];
      let clazz = [];
      let firstLine = "";
      for (let line of this.mappings.split("\n")) {
        if (line.startsWith("\t")) {
          clazz.push(line);
        } else {
          if (clazz.length > 0) {
            const header = firstLine.split(" ");
            arr.push({obfName: header[0], deobfName: header[1], lines: clazz});
            clazz = [];
            firstLine = line;
          } else {
            // first
            firstLine = line;
          }
        }
      }
      return arr;
    }
  },
  methods: {
    ...mapActions('mappings', ['getMappingsFor']),
    ...mapActions('merge', ['getMergedMappingsFor'])
  },
  async mounted() {
    if (!this.mappings) {
      if (this.$route.meta.merge) {
        await this.getMergedMappingsFor({leftType: this.$route.meta.leftType, rightType: this.$route.meta.rightType, version: this.version});
      } else {
        await this.getMappingsFor({provider: this.$route.meta.mappingType, version: this.version});
      }
    }
  }
};
</script>
