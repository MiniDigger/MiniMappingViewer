// import the api endpoints
import { getMappingsFor } from "@/api/mappings.api"
import Vue from "vue";

const state = {
  mappings: {}
}

const getters = {
  getMappings(state) {
    return state.mappings;
  }
}

const actions = {
  async getMappingsFor({ commit }, { provider, version }) {
    try {
      const response = await getMappingsFor(provider, version);
      commit('SET_MAPPINGS_FOR', {provider, version, data: response.data });
    } catch (error) {
      // handle the error here
    }
  }
}

const mutations = {
  SET_MAPPINGS_FOR(state, { provider, version, data }) {
    Vue.set(state.mappings, provider + "-" + version, data);
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
