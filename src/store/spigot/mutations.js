export function setVersions(state, { data }) {
  state.versions = data;
}
export function setMembers(state, { version, data }) {
  state.members[version] = data;
}
export function setClasses(state, { version, data }) {
  state.classes[version] = data;
}
