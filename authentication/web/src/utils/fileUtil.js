import { AUTHORIZATION, X_APP_ID } from "@/constant/HttpHeaderConst";
import LocalStorageUtil from "@/utils/LocalStorageUtil";

export function download(content, fileName) {
  // 获取响应数据
  const blob = new Blob(
    [content], // 将获取到的二进制文件转成blob
    { type: 'charset=utf-8' } // 有时候打开文档会出现乱码，设置一下字符编码
  );
  // 转成文件流之后，可以通过模拟点击实现下载效果
  const element = document.createElement('a');  // js创建一个a标签
  const href = window.URL.createObjectURL(blob);
  element.href = href;
  element.download = fileName;        // 下载后文件名
  document.body.appendChild(element);
  element.click();                    // 点击下载
  document.body.removeChild(element); // 下载完成移除元素
  window.URL.revokeObjectURL(href);
}

/**
 * 上传前填充http请求头
 * @param headers
 */
export function beforeUploadFillHttpHeader(headers) {
  // 请求头
  headers[AUTHORIZATION] = "Bearer " + LocalStorageUtil.getToken().accessToken
  headers[X_APP_ID] = 1; // 固定值
}
