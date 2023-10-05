// 按钮权限
import checkPermission from "@/utils/permission";

export default {
  // 指令第一次绑定到元素时调用。可以在这里进行一次性的初始化设置。
  bind(el, binding, vnode) {
  },
  // 被绑定元素插入父节点时调用。可以在这里进行DOM操作。
  inserted(el, binding, vnode) {
    const value = binding.value;
    if (!checkPermission(value)) {
      // 没有权限 移除Dom元素
      if (binding.modifiers['disable']) {
        console.log(value)
        // 设置鼠标事件失效
        el.classList.add("is-disabled")
        el.style.pointerEvents = "none";
      } else if (binding.modifiers['hide']) {
        el.style.zIndex = -9999
      } else {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  },
}
