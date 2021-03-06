import axios from 'axios';

const httpClient = axios.create({
  baseUrl: "/",
  timeout: 1000,
  headers: {
    "Content-Type": "application/json",
  }
});

export default httpClient;
