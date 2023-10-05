export default {
  bind(el, binding) {
    // 获取element-ui定义好的scroll盒子 表格滚动底部事件
    const TABLE_DOM = el.querySelector('.table-style .el-table__body-wrapper')
    TABLE_DOM.addEventListener('scroll', function() {
      const CONDITIONVALUE = this.scrollHeight - this.scrollTop <= this.clientHeight
      if (CONDITIONVALUE) {
        binding.value()
      }
    })
  }
}
