<template>
  <nav class="bg-gray-800 h-10 max-w flex content-center">
    <div class="ml-4">
      <button class="text-white">
        <fa-icon icon="bars" />
      </button>
    </div>
    <div class="ml-4">
      <h1 class="text-white">Mini's Mapping Viewer</h1>
    </div>
  </nav>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import HelloWorld from "@/components/HelloWorld.vue";

@Options({
  components: {
    HelloWorld,
  },
})
export default class Navigation extends Vue {}
</script>
