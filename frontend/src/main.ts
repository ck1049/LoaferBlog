import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 导入markdown编辑器
import VueMarkdownEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'

// 注册github主题
VueMarkdownEditor.use(githubTheme)

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(VueMarkdownEditor)

app.mount('#app')