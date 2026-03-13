<template>
  <div class="markdown-renderer" v-html="renderedContent"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { marked } from 'marked';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';

const props = defineProps<{
  content: string;
}>();

// 配置marked
const markedOptions: any = {
  highlight: function(code: string, lang: string) {
    const language = hljs.getLanguage(lang) ? lang : 'plaintext';
    return hljs.highlight(code, { language }).value;
  },
  breaks: true,
  gfm: true,
};

marked.setOptions(markedOptions);

const renderedContent = computed(() => {
  return marked(props.content);
});
</script>

<style scoped>
.markdown-renderer {
  line-height: 1.6;
  color: #333;
}

.markdown-renderer h1,
.markdown-renderer h2,
.markdown-renderer h3,
.markdown-renderer h4,
.markdown-renderer h5,
.markdown-renderer h6 {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
}

.markdown-renderer h1 {
  font-size: 2em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-renderer h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-renderer h3 {
  font-size: 1.25em;
}

.markdown-renderer p {
  margin-top: 0;
  margin-bottom: 16px;
}

.markdown-renderer code {
  background-color: #f6f8fa;
  border-radius: 3px;
  font-size: 85%;
  margin: 0;
  padding: 0.2em 0.4em;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
}

.markdown-renderer pre {
  background-color: #f6f8fa;
  border-radius: 3px;
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
  margin-bottom: 16px;
}

.markdown-renderer pre code {
  background-color: transparent;
  padding: 0;
  display: block;
}

.markdown-renderer blockquote {
  border-left: 4px solid #dfe2e5;
  color: #6a737d;
  padding: 0 1em;
  margin: 0 0 16px 0;
}

.markdown-renderer ul,
.markdown-renderer ol {
  margin-top: 0;
  margin-bottom: 16px;
  padding-left: 2em;
}

.markdown-renderer li {
  margin-bottom: 0.25em;
}

/* v-html 内的 img 无 scoped 属性，必须用 :deep() 才能生效 */
.markdown-renderer :deep(img) {
  max-width: 100% !important;
  max-height: 80vh !important;
  width: auto !important;
  height: auto !important;
  display: block !important;
  margin: 0 auto !important;
  object-fit: contain !important;
}

.markdown-renderer a {
  color: #0366d6;
  text-decoration: none;
}

.markdown-renderer a:hover {
  text-decoration: underline;
}

.markdown-renderer table {
  border-spacing: 0;
  border-collapse: collapse;
  margin-bottom: 16px;
  width: 100%;
  overflow: auto;
  display: block;
}

.markdown-renderer th {
  font-weight: 600;
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
  background-color: #f6f8fa;
}

.markdown-renderer td {
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
}

.markdown-renderer tr {
  background-color: #fff;
  border-top: 1px solid #c6cbd1;
}

.markdown-renderer tr:nth-child(2n) {
  background-color: #f6f8fa;
}
</style>
