const {createProxyMiddleware} = require('http-proxy-middleware');
const {proxying} = require('../package.json');

module.exports = function (app) {

  const {host, port} = proxying

  app.use(
      '/api',
      createProxyMiddleware({
        target: `http://${host}:${port}`,
        changeOrigin: true
      })
  )
}
