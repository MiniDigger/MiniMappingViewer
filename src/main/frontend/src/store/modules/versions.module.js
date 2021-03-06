// import the api endpoints
import { getVersionsFor } from "@/api/versions.api"
import Vue from "vue";

const state = {
  versions: {}
}

const getters = {
  getVersions(state) {
    return state.versions;
  }
}

const actions = {
  async getVersionsFor({ commit }, version) {
    try {
      const response = await getVersionsFor(version);
      commit('SET_VERSIONS_FOR', {version, data: response.data });
    } catch (error) {
      // todo handle the error here
      console.log(error);
    }
  }
}

const mutations = {
  SET_VERSIONS_FOR(state, { version, data }) {
    Vue.set(state.versions, version, data);
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
