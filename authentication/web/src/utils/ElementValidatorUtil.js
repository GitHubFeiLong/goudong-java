module.exports = {
  WebUrl(rule, value, callback) {
    let reg = /^(http[s]?:\/\/)(.*)$/
    matching(value, callback, reg, '请输入正确的链接')
  }, Username(rule, value, callback) {
    let reg = /^(?![0-9]*$)(?![a-zA-Z]*$)[a-zA-Z0-9]{6,12}$/
    matching(value, callback, reg, '请输入6-12位字母和数字组合')
  }, SimplePwd(rule, value, callback) {
    let reg = /^[_a-zA-Z0-9]+$/
    matching(value, callback, reg, '包含英文字母、数字及下划线组成')
  }, ComplexPwd(rule, value, callback) {
    let reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$)([^\u4e00-\u9fa5\s]){8,20}$/
    matching(value, callback, reg, '请输入8-20位英文字母、数字或者符号')
  }, Phone(rule, value, callback) {
    let reg = /^[1][3, 4, 5, 6, 7, 8][0-9]{9}$/
    matching(value, callback, reg, '请输入正确的手机')
  }, Email(rule, value, callback) {
    let reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/
    matching(value, callback, reg, '输入正确的邮箱')
  }, IdCard(rule, value, callback) {
    let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
    matching(value, callback, reg, '输入正确的身份证号码')
  }, Company(rule, value, callback) {
    let reg = /^[A-Z0-9]{8}-[A-Z0-9]$|^[A-Z0-9]{8}-[A-Z0-9]-[0-9]{2}$/
    matching(value, callback, reg, '请输入正确的营业执照')
  }, Weixin(rule, value, callback) {
    let reg = /^[a-zA-Z][a-zA-Z0-9_-]{5,19}$/
    matching(value, callback, reg, '请输入正确的微信号')
  }, Integer(rule, value, callback) {
    let reg = /^[1-9][0-9]*$/
    matching(value, callback, reg, '请输入正确的整数')
  }, Number(rule, value, callback) {
    let reg = /^\d+$|^\d+[.]?\d+$/
    matching(value, callback, reg, '请输入纯数字')
  }, Landline(rule, value, callback) {
    let reg = /^(\d{3,4}-)?\d{7,8}$/
    matching(value, callback, reg, '请输入正确的座机')
  }, Ip(rule, value, callback) {
    let reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    matching(value, callback, reg, '请输入正确的IP')
  }, Price(rule, value, callback) {
    let reg = /^-?\d{1,4}(?:\.\d{1,2})?$ /
    matching(value, callback, reg, '请输入正确的价格')
  }, BankCard(rule, value, callback) {
    let reg = /^([1-9]{1})(\d{14}|\d{18})$/
    matching(value, callback, reg, '请输入正确的银行卡')
  }
}

/**
 * 匹配函数
 * @param value 校验的值
 * @param callback 校验后回调
 * @param reg 正则
 * @param message 消息
 */
let matching = (value, callback, reg, message) => {
  if (value === '' || value === undefined || value == null) {
    callback(new Error(message))
  } else {
    if (!reg.test(value)) {
      callback(new Error(message))
    } else {
      callback()
    }
  }
}
