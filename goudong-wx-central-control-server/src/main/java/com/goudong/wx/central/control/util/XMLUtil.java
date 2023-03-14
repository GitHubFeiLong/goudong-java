package com.goudong.wx.central.control.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * XML工具类
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 15:08
 */
public class XMLUtil {

    /**
     * 将输入流转成map集合
     * @param is
     * @return
     */
    public static Map<String, String> toMap(InputStream is) {
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            document = saxReader.read(is);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element root = document.getRootElement();
        List<Element> elements = root.elements();

        Map<String, String> map = new HashMap<>(elements.size());
        for(Element e : elements) {
            map.put(e.getName(), e.getStringValue());
        }

        return map;
    }
}
