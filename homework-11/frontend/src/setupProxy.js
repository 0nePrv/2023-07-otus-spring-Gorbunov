const {createProxyMiddleware} = require('http-proxy-middleware');

module.exports = function (app) {

  const [host, port] = ['localhost', 8080]

  app.use(
      '/api',
      createProxyMiddleware({
        target: `http://${host}:${port}`,
        changeOrigin: true
      })
  )
}
