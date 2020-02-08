import Vue from "vue";
import Vuex from "vuex";

import mojang from "./mojang";
import spigot from "./spigot";

Vue.use(Vuex);

const Store = new Vuex.Store({
  modules: {
    mojang,
    spigot
  },
  strict: process.env.DEV
});

export { Store };

export default (/* { ssrContext } */) => {
  return Store;
};
