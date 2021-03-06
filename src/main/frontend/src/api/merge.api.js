import httpClient from './httpClient';

const END_POINT = '/api/merge/';

const getMergedMappingsFor = (leftType, rightType, version) => httpClient.get(END_POINT + version + "/" + leftType + "/" + rightType);

export {
  getMergedMappingsFor
}
