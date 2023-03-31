package com.goudong.file.controller.download;

import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.file.po.FilePO;
import com.goudong.file.repository.FileRepository;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 类描述：
 * 下载
 * @author msi
 * @version 1.0
 * @date 2022/2/21 21:13
 */
@Api(tags = "文件下载")
@Slf4j
@Validated
@RestController
@RequestMapping("/download-group")
public class DownloadController {

    //~fields
    //==================================================================================================================
    private final FileRepository fileRepository;

    /**
     * 请求对象
     */
    private final HttpServletRequest request;

    /**
     * 响应对象
     */
    private final HttpServletResponse response;

    //~methods
    //==================================================================================================================


    public DownloadController(FileRepository fileRepository, HttpServletRequest request, HttpServletResponse response) {
        this.fileRepository = fileRepository;
        this.request = request;
        this.response = response;
    }

    /**
     * 下载文件
     * range请求头使用https://www.cnblogs.com/1995hxt/p/5692050.html
     * Range: bytes=10- ：第10个字节及最后个字节的数据;Range: bytes=40-100 ：第40个字节到第100个字节之间的数据.
     * @param range (详细描述：https://www.cnblogs.com/1995hxt/p/5692050.html)
     * @param fileId 文件上传时生成的访问地址
     */
    @GetMapping("/download")
    @Whitelist
    void download(@RequestHeader(required = false) String range, Long fileId) {
        // 被下载的文件
        FilePO filePO = fileRepository.findById(fileId)
                .orElseThrow(() -> new ClientException(ClientExceptionEnum.NOT_FOUND, "文件不存在", "文件fileId查询不到数据"));

        File file = new File(filePO.getFilePath());
        if (!file.exists()) {
            throw new ClientException(ClientExceptionEnum.NOT_FOUND, "文件不存在", "文件服务器上未发现该文件");
        }

        //开始下载位置
        long startByte = 0;
        //结束下载位置（数组下标从0开始）
        long endByte = file.length() - 1;

        // range值格式正确
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            //坑爹地方一：http状态码要为206
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                //判断range的类型
                if (ranges.length == 1) {
                    //类型一：bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //类型二：bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }

            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
            }
        } else {
            //没有ranges即全部一次性传输，需要用200状态码，这一行应该可以省掉，因为默认返回是200状态码
            response.setStatus(HttpServletResponse.SC_OK);
        }

        //要下载的长度（endByte为总长度-1，这时候要加回去）
        long contentLength = endByte - startByte + 1;
        //文件名
        String fileName = filePO.getOriginalFilename();
        //文件类型
        String contentType = request.getServletContext().getMimeType(fileName);


        //各种响应头设置
        //参考资料：https://www.ibm.com/developerworks/cn/java/joy-down/index.html
        //坑爹地方二：看代码
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(contentType);
        // response.setHeader("Content-Type", contentType);
        // response.setContentType("application/x-msdownload");
        //这里文件名换你想要的，inline表示浏览器可以直接使用（比如播放音乐，我方便测试用的）
        //参考资料：http://hw1287789687.iteye.com/blog/2188500
        // response.setHeader("Content-Disposition", "inline;filename=test.mp3");
        response.setHeader("Content-Disposition", "attachment;filename=1.png");
        response.setHeader("Content-Length", String.valueOf(contentLength));
        //坑爹地方三：Content-Range，格式为
        // [要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());


        BufferedOutputStream outputStream;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            //坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //不然会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
                //停一下，方便测试（本地下载传输速度特别快，没反应过来就下载好了），实际生产环境中用的时候需要删除这一行
                // Thread.sleep(10);
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            System.out.println("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);

        } catch (ClientAbortException e) {
            System.out.println("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            //捕获此异常表示拥护停止下载
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
