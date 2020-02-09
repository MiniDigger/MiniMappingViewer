const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/Index.vue") },
      { path: "mojang", component: () => import("pages/MojangIndex.vue") },
      {
        path: "mojang/client/:versionId",
        component: () => import("pages/MojangClient.vue")
      },
      {
        path: "mojang/server/:versionId",
        component: () => import("pages/MojangServer.vue")
      },
      { path: "spigot", component: () => import("pages/SpigotIndex.vue") },
      {
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
