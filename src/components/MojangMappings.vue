<template>
  <q-page>
    <div class="row">
      <h3 v-if="client" class="col-8" style="margin: 10px 0 10px 0">
        Client Mojang Mappings for {{ versionId }}
      </h3>
      <h3 v-if="!client" class="col-8" style="margin: 10px 0 10px 0">
        Mojang -> Spigot for {{ versionId }}
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
          :items="mojangKeys"
          style="max-height: calc(100vh - 120px)"
          :virtual-scroll-slice-size="15"
          separator
        >
          <template v-slot="{ item, index }">
            <q-item :key="index">
              <Member v-if="client" class="col-12"
                      :mojangData="getMojangItemData(item)"
                      :toObf="true"/>
              <Member v-else class="col-12"
                      :mojangData="getMojangItemData(item)"
                      :spigotData="getSpigotItemData(item)"
                      :toObf="true"
              />
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
import { parseMojang } from "src/api/mojang";
import { parseSpigot } from "src/api/spigot";
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
      mojangParsed: null,
      spigotParsed: null,
      filter: ""
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
      spigotVersions: state => state.spigot.versions,
      spigotClasses: state => state.spigot.classes,
      spigotMembers: state => state.spigot.members
    }),
    mojangKeys() {
      return this.mojangParsed
        ? Object.keys(this.mojangParsed.mappedToObf).filter(
            k => k.toLowerCase().indexOf(this.filter.toLowerCase()) > -1
          )
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
    getMojangItemData(key) {
      let result = this.mojangParsed.mappedToObf[key];
      return result ? result : {};
    },
    getSpigotItemData(key) {
      let mojangData = this.getMojangItemData(key);
      if (mojangData) {
        let spigotData = this.spigotParsed.obfToMapped[mojangData.obf];
        if (spigotData) {
          // let result = this.spigotParsed.mappedToObf[spigotData.mapped];
          // return result ? result : {};
          return spigotData;
        } else {
          return {};
        }
      } else {
        return {};
      }
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
                this.mojangParsed = parseMojang(
                  this.clientMappings[this.versionId]
                );
              } else {
                this.mojangParsed = parseMojang(
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
      this.loadSpigotMappings({ versionId: this.versionId })
        .then(() => {
          this.spigotParsed = parseSpigot(
            this.spigotClasses[this.versionId],
            this.spigotMembers[this.versionId]
          );
        })
        .catch(error => {
          sendError("Error while loading spigot mappings: " + error);
        });
    }
  }
};
</script>

<style scoped></style>
