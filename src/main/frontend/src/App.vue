<template>
  <v-app>
    <v-app-bar
      color="amber"
      dark
    >
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>

      <v-toolbar-title>Mini's Mapping Viewer</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn href="https://github.com/MiniDigger/MiniMappingViewer">GITHUB</v-btn>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      absolute
      temporary
    >
      <v-list
        nav
        dense
      >
        <v-list-item-group
          v-model="group"
          active-class="amber--text text--accent-4"
        >
          <v-list-item v-for="route in routes" :to="route.path" :key="route.path">
            <v-list-item-icon>
              <v-icon>{{ route.meta.icon }}</v-icon>
            </v-list-item-icon>
            <v-list-item-title>{{ route.name }}</v-list-item-title>
          </v-list-item>

          <v-divider/>

          <v-list-item>
            <v-list-item-icon>
              <v-icon v-if="$vuetify.theme.dark">mdi-brightness-5</v-icon>
              <v-icon v-else>mdi-brightness-7</v-icon>
            </v-list-item-icon>
            <v-list-item-title v-if="$vuetify.theme.dark" @click="$vuetify.theme.dark = false">Dark Mode
            </v-list-item-title>
            <v-list-item-title v-else @click="$vuetify.theme.dark = true">Light Mode</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <RouterView/>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",
  data: () => ({
    drawer: false,
    group: null
  }),
  computed: {
    routes() {
      return this.$router.options.routes.filter(r => r.meta.hide !== true);
    }
  }
};
</script>

<style lang="scss">
// for some reason this breaks shit
header.v-toolbar {
  flex: none;
}
</style>
