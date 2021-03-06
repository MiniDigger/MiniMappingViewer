import httpClient from './httpClient';

const END_POINT = '/api/versions/';

const getVersionsFor = (version) => httpClient.get(END_POINT + version);

export {
  getVersionsFor
}
