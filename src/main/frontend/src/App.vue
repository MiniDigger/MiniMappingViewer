<template>
  <Navigation />
  <div class="container">
    <router-view />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Navigation from "@/components/Navigation.vue";

@Options({
  components: {
    Navigation,
  },
})
export default class App extends Vue {}
</script>
