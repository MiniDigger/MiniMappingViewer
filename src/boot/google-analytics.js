import ga from "../api/analytics"

export default ({ router }) => {
  router.afterEach((to, from) => {
    ga.logPage(to.path, to.name, to.params.versionId)
  })
}
