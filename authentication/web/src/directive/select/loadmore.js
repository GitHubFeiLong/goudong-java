// 下拉分页
export default {
  bind(el, binding) {
    // 获取element-ui定义好的scroll盒子 下拉选框滚动底部事件
    const SELECTWRAP_DOM = el.querySelector('.el-select-dropdown .el-select-dropdown__wrap')
    SELECTWRAP_DOM.addEventListener('scroll', function() {
      const CONDITION = this.scrollHeight - this.scrollTop <= this.clientHeight
      if (CONDITION) {
        binding.value()
      }
    })
  }
}
