import Vue from 'vue'

import loadmore from './loadmore'
import moretable from './moretable'

const install = function(Vue) {
  Vue.directive('loadmore', loadmore)
  Vue.directive('moretable', moretable)
}

if (window.Vue) {
  window['loadmore'] = loadmore
  window['moretable'] = moretable
  Vue.use(install); // eslint-disable-line
}

loadmore.install = install
moretable.install = install
export {
  loadmore,
  moretable
}
