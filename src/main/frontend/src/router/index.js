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
  }
];

const types = [
  {name: "Spigot", type: "SPIGOT", path: "spigot"},
  {name: "Mojang", type: "MOJANG", path: "mojang"},
  {name: "Yarn", type: "YARN", path: "yarn"},
];

types.forEach(type => {
  addIndexAndVersion(type.name, type.type, type.path);
  types.filter(t => t.name !== type.name).forEach(inner => {
    addMerge(type, inner);
  });
});

function addMerge(left, right) {
  routes.push({
    path: "/" + left.path + "-" + right.path,
    name: left.name + " -> " + right.name,
    component: () => import(/* webpackChunkName: "index" */ "../views/MappingIndex.vue"),
    meta: {
      icon: "mdi-home",
      merge: true,
      leftType: left.type,
      rightType: right.type,
      mappingType: left.type + "-" + right.type
    }
  });
  routes.push({
    path: "/" + left.path + "-" + right.path + "/:version",
    name: left.name + " -> " + right.name + "!",
    component: () => import(/* webpackChunkName: "view" */ "../views/MappingView.vue"),
    meta: {
      icon: "mdi-home",
      hide: true,
      merge: true,
      leftType: left.type,
      rightType: right.type,
      mappingType: left.type + "-" + right.type
    },
  });
}

function addIndexAndVersion(name, type, path) {
  routes.push({
    path: "/" + path,
    name: name,
    component: () => import(/* webpackChunkName: "index" */ "../views/MappingIndex.vue"),
    meta: {
      icon: "mdi-home",
      mappingType: type
    }
  });
  routes.push({
    path: "/" + path + "/:version",
    name: name + "!",
    component: () => import(/* webpackChunkName: "view" */ "../views/MappingView.vue"),
    meta: {
      icon: "mdi-home",
      hide: true,
      mappingType: type
    },
  });
}

const router = new VueRouter({
  routes
});

export default router;
