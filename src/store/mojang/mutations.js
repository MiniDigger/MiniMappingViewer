export function setVersionManifest(state, { data }) {
  state.versionManifest = data;
}
export function setVersion(state, { version, data }) {
  state.versions[version] = data;
}
export function setClientMapping(state, { version, data }) {
  state.clientMappings[version] = data;
}
export function setServerMapping(state, { version, data }) {
  state.serverMappings[version] = data;
}
