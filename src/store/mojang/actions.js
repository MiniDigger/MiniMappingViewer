import axios from "axios";

const corsanywhere = "https://corsanywhere.benndorf.dev/";
const versionManifestUrl =
  "https://launchermeta.mojang.com/mc/game/version_manifest.json";

export const loadVersions = ({ commit }) => {
  return new Promise((resolve, reject) => {
    axios
      .get(versionManifestUrl)
      .then(response => {
        if (response && response.status === 200 && response.data) {
          commit("setVersionManifest", { data: response.data });
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

export const loadVersion = ({ commit, state }, { versionId }) => {
  return new Promise((resolve, reject) => {
    const version = state.versionManifest.versions.find(
      v => v.id === versionId
    );
    if (version) {
      axios
        .get(version.url)
        .then(response => {
          if (response && response.status === 200 && response.data) {
            commit("setVersion", { version: versionId, data: response.data });
            resolve(response.data);
          } else {
            reject("Error: " + response);
          }
        })
        .catch(error => {
          reject(error);
        });
    } else {
      reject("Version not found");
    }
  });
};

export const loadMappings = ({ commit, state }, { versionId }) => {
  return new Promise((resolve, reject) => {
    const version = state.versions ? state.versions[versionId] : null;
    if (version) {
      const clientMappingUrls = version.downloads.client_mappings.url;
      const serverMappingUrls = version.downloads.server_mappings.url;

      let otherFinished = false;
      axios.get(corsanywhere + clientMappingUrls).then(response => {
        if (response && response.status === 200 && response.data) {
          commit("setClientMapping", {
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

      axios.get(corsanywhere + serverMappingUrls).then(response => {
        if (response && response.status === 200 && response.data) {
          commit("setServerMapping", {
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
