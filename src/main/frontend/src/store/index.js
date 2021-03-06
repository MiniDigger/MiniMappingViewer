import Vue from "vue";
import Vuex from "vuex";
import versions from "@/store/modules/versions.module";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    versions: versions
  }
});
