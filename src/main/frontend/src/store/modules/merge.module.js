// import the api endpoints
import { getMergedMappingsFor } from "@/api/merge.api"
import Vue from "vue";

const state = {
  mergedMappings: {}
}

const getters = {
  getMergedMappings(state) {
    return state.mergedMappings;
  }
}

const actions = {
  async getMergedMappingsFor({ commit }, { leftType, rightType, version }) {
    try {
      const response = await getMergedMappingsFor(leftType, rightType, version);
      commit('SET_MERGED_MAPPINGS_FOR', {leftType, rightType, version, data: response.data });
    } catch (error) {
      // todo handle the error here
      console.log(error);
    }
  }
}

const mutations = {
  SET_MERGED_MAPPINGS_FOR(state, { leftType, rightType, version, data }) {
    Vue.set(state.mergedMappings, leftType + "-" + rightType + "-" + version, data);
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
