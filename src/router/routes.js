const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { name: "Index", path: "", component: () => import("pages/Index.vue") },
      { name: "MojangIndex", path: "mojang", component: () => import("pages/MojangIndex.vue") },
      {
        name: "MojangClient",
        path: "mojang/client/:versionId",
        component: () => import("pages/MojangClient.vue")
      },
      {
        name: "MojangServer",
        path: "mojang/server/:versionId",
        component: () => import("pages/MojangServer.vue")
      },
      { name: "SpigotIndex", path: "spigot", component: () => import("pages/SpigotIndex.vue") },
      {
        name: "SpigotServer",
        path: "spigot/server/:versionId",
        component: () => import("pages/SpigotServer.vue")
      }
    ]
  }
];

// Always leave this as last one
if (process.env.MODE !== "ssr") {
  routes.push({
    path: "*",
    component: () => import("pages/Error404.vue")
  });
}

export default routes;
