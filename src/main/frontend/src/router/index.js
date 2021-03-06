import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: () => import(/* webpackChunkName: "home" */ "../views/Home.vue"),
    meta: {
      icon: "mdi-home",
    },
  },
  {
    path: "/spigot",
    name: "Spigot",
    component: () => import(/* webpackChunkName: "index" */ "../views/MappingIndex.vue"),
    meta: {
      icon: "mdi-home",
      mappingType: "SPIGOT"
    },
  },
  {
    path: "/spigot/:version",
    name: "SpigotView",
    component: () => import(/* webpackChunkName: "view" */ "../views/MappingView.vue"),
    meta: {
      icon: "mdi-home",
      hide: true,
      mappingType: "SPIGOT"
    },
  },
  {
    path: "/mojang",
    name: "Mojang",
    component: () => import(/* webpackChunkName: "index" */ "../views/MappingIndex.vue"),
    meta: {
      icon: "mdi-home",
      mappingType: "MOJANG"
    },
  },
  {
    path: "/mojang/:version",
    name: "MojangView",
    component: () => import(/* webpackChunkName: "view" */ "../views/MappingView.vue"),
    meta: {
      icon: "mdi-home",
      hide: true,
      mappingType: "MOJANG"
    },
  },
  {
    path: "/yarn",
    name: "Yarn",
    component: () => import(/* webpackChunkName: "index" */ "../views/MappingIndex.vue"),
    meta: {
      icon: "mdi-home",
      mappingType: "YARN"
    },
  },
  {
    path: "/yarn/:version",
    name: "YarnView",
    component: () => import(/* webpackChunkName: "view" */ "../views/MappingView.vue"),
    meta: {
      icon: "mdi-home",
      hide: true,
      mappingType: "YARN"
    },
  },
];

const router = new VueRouter({
  routes
});

export default router;
