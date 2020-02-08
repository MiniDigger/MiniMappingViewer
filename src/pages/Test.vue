<template>
  <q-page style="max-height: calc(100vh - 50px)">
    <div class="row" style="max-height: calc(100vh - 50px)">
      <h2 class="col-8" style="margin: 10px 0 10px 0">Test</h2>
      <q-input
        v-model="filter"
        @input="input"
        @clear="clear"
        placeholder="Filter (Classes)"
        borderless
        clearable
        dense
        class="col-4"
        type="search"
      >
        <template v-slot:append>
          <q-icon name="search" />
        </template>
      </q-input>

      <div class="col-12">
        <q-virtual-scroll
          ref="scroll"
          :items="keys"
          style="max-height: calc(100vh - 50px - 80px)"
          :virtual-scroll-slice-size="15"
          separator
        >
          <template v-slot="{ item, index }">
            <q-item :key="index">
              <Member :data="getItemData(item)" :toObf="toObf" />
            </q-item>
          </template>
        </q-virtual-scroll>
      </div>
    </div>
  </q-page>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { sendError } from "src/api/notify";
import { parseMojang } from "src/api/mappings";
import Member from "components/Member";

export default {
  name: "Test",
  components: { Member },
  data() {
    return {
      parsedData: null,
      filter: "",
      toObf: true
    };
  },
  mounted() {
    let versionId = "1.15.2";
    this.loadMojangVersions()
      .then(() => {
        this.loadMojangVersion({ versionId: versionId })
          .then(() => {
            this.loadMojangMappings({ versionId: versionId })
              .then(() => {
                this.parsedData = parseMojang(this.serverMappings[versionId]);
              })
              .catch(error => {
                sendError("Error while loading mojang mappings: " + error);
              });
          })
          .catch(error => {
            sendError("Error while loading mojang version: " + error);
          });
      })
      .catch(error => {
        sendError("Error while loading mojang versions: " + error);
      });

    this.loadSpigotVersions()
      .then(() => {
        this.loadSpigotMappings({ versionId: versionId }).catch(error => {
          sendError("Error while loading spigot mappings: " + error);
        });
      })
      .catch(error => {
        sendError("Error while loading spigot versions: " + error);
      });
  },
  computed: {
    ...mapState({
      versions: state => state.mojang.versions,
      clientMappings: state => state.mojang.clientMappings,
      serverMappings: state => state.mojang.serverMappings
    }),
    keys() {
      return this.parsedData
        ? Object.keys(
            this.toObf
              ? this.parsedData.mojangToObf
              : this.parsedData.obfToMojang
          ).filter(k => k.toLowerCase().indexOf(this.filter.toLowerCase()) > -1)
        : [];
    }
  },
  methods: {
    ...mapActions({
      loadMojangVersions: "mojang/loadVersions",
      loadMojangVersion: "mojang/loadVersion",
      loadMojangMappings: "mojang/loadMappings",

      loadSpigotVersions: "spigot/loadVersions",
      loadSpigotMappings: "spigot/loadMappings"
    }),
    getItemData(key) {
      return this.toObf
        ? this.parsedData.mojangToObf[key]
        : this.parsedData.obfToMojang[key];
    },
    clear() {
      this.filter = "";
      this.$refs.scroll.reset();
    },
    input() {
      this.$refs.scroll.reset();
    }
  }
};
</script>

<style scoped></style>
