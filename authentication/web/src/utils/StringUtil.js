//这个是写的公共方法
/** 字符串工具*/

/**
 * 拷贝字符串
 * @param value
 * @param that
 */
export const copyText = function(value, that) {
  const aux = document.createElement('input')
  aux.setAttribute('value', value)
  document.body.appendChild(aux)
  aux.select()
  document.execCommand('copy')
  document.body.removeChild(aux)
  that.$message.success('复制成功')
}
