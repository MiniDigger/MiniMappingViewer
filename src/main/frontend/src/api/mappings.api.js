import httpClient from './httpClient';

const END_POINT = '/api/mappings/';

const getMappingsFor = (provider, version) => httpClient.get(END_POINT + provider + "/" + version);

export {
  getMappingsFor
}
