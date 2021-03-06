import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import colors from 'vuetify/lib/util/colors'

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: colors.amber.darken1,
        secondary: colors.amber.lighten3,
        accent: colors.indigo.base,
      },
      dark: {
        primary: colors.amber.darken3,
        secondary: colors.amber.darken1,
        accent: colors.indigo.base,
      }
    },
    // options: {
    //   themeCache: {
    //     get: key => localStorage.getItem(key),
    //     set: (key, value) => localStorage.setItem(key, value),
    //   },
    // }
  },
})
