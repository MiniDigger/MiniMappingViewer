<template>
  <q-page style="max-height: calc(100vh - 50px)">
    <div class="row" style="max-height: calc(100vh - 50px)">
      <h3 class="col-8" style="margin: 10px 0 10px 0">
        Displaying Mojang Mappings for {{ versionId }}
      </h3>
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
          style="max-height: calc(100vh - 50px - 50px - 20px)"
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
import Member from "components/display/Member";

export default {
  name: "MojangMappings",
  components: { Member },
  props: {
    versionId: {
      type: String,
      required: true
    },
    client: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      parsedData: null,
      filter: "",
      toObf: true
    };
  },
  mounted() {
    if (this.versionManifest) {
      this.loadMojang();
    } else {
      this.loadMojangVersions()
        .then(() => {
          this.loadMojang();
        })
        .catch(error => {
          sendError("Error while loading mojang versions: " + error);
        });
    }

    if (!this.client) {
      if (this.spigotVersions) {
        this.loadSpigot();
      } else {
        this.loadSpigotVersions()
          .then(() => {
            this.loadSpigot();
          })
          .catch(error => {
            sendError("Error while loading spigot versions: " + error);
          });
      }
    }
  },
  computed: {
    ...mapState({
      versionManifest: state => state.mojang.versionManifest,
      versions: state => state.mojang.versions,
      clientMappings: state => state.mojang.clientMappings,
      serverMappings: state => state.mojang.serverMappings,
      spigotVersions: state => state.spigot.versions
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
    },
    loadMojang() {
      this.loadMojangVersion({ versionId: this.versionId })
        .then(() => {
          this.loadMojangMappings({ versionId: this.versionId })
            .then(() => {
              if (this.client) {
                this.parsedData = parseMojang(
                  this.clientMappings[this.versionId]
                );
              } else {
                this.parsedData = parseMojang(
                  this.serverMappings[this.versionId]
                );
              }
            })
            .catch(error => {
              sendError("Error while loading mojang mappings: " + error);
            });
        })
        .catch(error => {
          sendError("Error while loading mojang version: " + error);
        });
    },
    loadSpigot() {
      this.loadSpigotMappings({ versionId: this.versionId }).catch(error => {
        sendError("Error while loading spigot mappings: " + error);
      });
    }
  }
};
</script>

<style scoped></style>
