import axios from "axios";

const corsanywhere = "https://corsanywhere.minidigger.me/";
const versionsGist =
  "https://gist.githubusercontent.com/MiniDigger/6c483628f4745b1b326862acb89a82d2/raw/69792f62c217fa472c84ee59bff142639e0463f4/builddata.json";

export const loadVersions = ({ commit }) => {
  return new Promise((resolve, reject) => {
    axios
      .get(versionsGist)
      .then(response => {
        if (response && response.status === 200 && response.data) {
          Object.keys(response.data).forEach(key => response.data[key].id = key);
          commit("setVersions", { data: response.data });
          resolve(response.data);
        } else {
          reject("Error: " + response);
        }
      })
      .catch(error => {
        reject(error);
      });
  });
};

export const loadMappings = ({ commit, state }, { versionId }) => {
  return new Promise((resolve, reject) => {
    const version = state.versions ? state.versions[versionId] : null;
    if (version) {
      const classesUrl = version.classes;
      const membersUrl = version.members;

      let otherFinished = false;
      axios.get(corsanywhere + classesUrl).then(response => {
        if (response && response.status === 200 && response.data) {
          commit("setClasses", {
            version: versionId,
            data: response.data
          });
          if (otherFinished) {
            resolve();
          } else {
            otherFinished = true;
          }
        } else {
          reject("Error: " + response);
        }
      });

      axios.get(corsanywhere + membersUrl).then(response => {
        if (response && response.status === 200 && response.data) {
          commit("setMembers", {
            version: versionId,
            data: response.data
          });
          if (otherFinished) {
            resolve();
          } else {
            otherFinished = true;
          }
        } else {
          reject("Error: " + response);
        }
      });
    } else {
      reject("Version not found");
    }
  });
};
