/** 一些公用的信息*/

// 接口前缀
export const API_PREFIX = "/api/authentication-server";
/**
 * 用户下拉选
 */
export const SEX_DROP_DOWN_OPTION = [
  { label: "未知", value: "0" },
  { label: "男性", value: "1" },
  { label: "女性", value: "2" },
]
/**
 * app应用的状态
 */
export const ENABLED_ARRAY = [
  { label: '未激活', value: 0 },
  { label: '已激活', value: 1 },
];

/**
 * http 请求方法
 */
export const HTTP_METHOD_ARRAY = [
  { value: 'GET', label: 'GET' },
  { value: 'POST', label: 'POST' },
  { value: 'PUT', label: 'PUT' },
  { value: 'DELETE', label: 'DELETE' },
  { value: 'HEAD', label: 'HEAD' },
  { value: 'PATCH', label: 'PATCH' },
  { value: 'OPTIONS', label: 'OPTIONS' },
  { value: 'TRACE', label: 'TRACE' },
];

/**
 * 日期组件的显示样式
 */
export const DATE_PICKER_DEFAULT_OPTIONS = {
  shortcuts: [{
    text: '最近一周',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      picker.$emit('pick', [start, end])
    }
  }]
}

/**
 * 菜单类型
 */
export const MENU_TYPE_ARRAY = [
  { label: '菜单', value: 1 },
  { label: '按钮', value: 2 },
  { label: '接口', value: 3 },
]
