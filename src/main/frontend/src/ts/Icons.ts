import { App } from 'vue'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faBars } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'


export default function(vue: App) {
    library.add(faBars)

    vue.component('fa-icon', FontAwesomeIcon)
}
