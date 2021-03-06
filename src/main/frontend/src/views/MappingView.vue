<template>
  <v-row>
    <v-col cols="12" md="6" offset="3">
      <v-card class="ma-4">
        <v-card-title>
          <h1>Mapping View for {{ mappingType }} on {{ version }}</h1>
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
    mappingType() {
      return this.$route.meta.mappingType;
    },
    version() {
      return this.$route.params.version;
    },
    mappings() {
      return this.getMappings[this.mappingType + "-" + this.version];
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
    ...mapActions('mappings', ['getMappingsFor'])
  },
  async mounted() {
    if (!this.mappings) {
      await this.getMappingsFor({provider: this.mappingType, version: this.version});
    }
  }
};
</script>
