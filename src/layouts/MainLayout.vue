<template>
  <q-layout view="hHh Lpr lff">
    <q-header elevated class="bg-black">
      <q-toolbar>
        <q-btn flat @click="drawer = !drawer" round dense icon="menu" aria-label="Menu"/>
        <q-toolbar-title>Mini's Mapping Viewer</q-toolbar-title>
        <q-btn
          flat
          round
          dense
          @click="openUrl('https://github.com/MiniDigger/MiniMappingViewer/')"
          >Github</q-btn
        >
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="drawer"
      show-if-above
      :mini="miniState"
      @mouseover="miniState = false"
      @mouseout="miniState = true"
      :width="200"
      :breakpoint="500"
      bordered
    >
      <q-scroll-area class="fit">
        <q-list padding>
          <q-item clickable v-ripple to="/mojang" aria-label="Spigot">
            <q-item-section avatar>
              <q-icon name="play_arrow" />
            </q-item-section>

            <q-item-section>
              Mojang -> Spigot
            </q-item-section>
          </q-item>
          <q-item clickable v-ripple to="/spigot" aria-label="Spigot">
            <q-item-section avatar>
              <q-icon name="play_arrow" />
            </q-item-section>

            <q-item-section>
              Spigot -> Mojang
            </q-item-section>
          </q-item>
          <q-item clickable v-ripple aria-label="Toggle Dark Mode" @click="toggleDark">
            <q-item-section avatar>
              <q-icon name="brightness_5" v-if="darkMode"/>
              <q-icon name="brightness_2" v-else />
            </q-item-section>

            <q-item-section v-if="darkMode">
              Light Mode
            </q-item-section>
            <q-item-section v-else>
              Dark Mode
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
  import { openURL } from 'quasar'


  export default {
  name: "MainLayout",
  data() {
    return {
      drawer: false,
      miniState: true
    };
  },
  computed: {
    darkMode() {
      return this.$q.dark.isActive;
    }
  },
  methods: {
    openUrl(url) {
      openURL(url);
    },
    toggleDark() {
      this.$q.dark.toggle();
    }
  }
};
</script>
