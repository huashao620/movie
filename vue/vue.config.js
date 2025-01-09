const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  devServer: {
    port:8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8999',
        ChangeOrigin: true,
        secure: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  transpileDependencies: true,
  runtimeCompiler: true
})
