package com.goudong.wx.central.control.dto.req;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建菜单
 * @author cfl
 * @version 1.0
 * @date 2023/3/16 20:10
 */
@Data
public class CreateMenuReq implements Serializable {
    //~fields
    //==================================================================================================================
    /**
     * 一级菜单数组，个数应为1~3个
     */
    private List<AbstractButton> button;

    //~methods
    //==================================================================================================================

    public CreateMenuReq(List<AbstractButton> button) {
        this.button = button;
    }

    /**
     * 类描述：
     * 按钮抽象类
     * @author cfl
     * @date 2023/3/16 20:11
     * @version 1.0
     */
    @Data
    public abstract static class AbstractButton implements Serializable{
        /**
         * 按钮名字<br/>
         * 一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以“...”代替
         */
        protected String name;

        /**
         * 二级菜单
         */
        protected List<AbstractButton> subButton;

        private AbstractButton() {
        }

        public AbstractButton(String name) {
            this.name = name;
        }
    }

    /**
     * 类描述：
     * 二级菜单
     * @author cfl
     * @date 2023/3/16 21:03
     * @version 1.0
     */
    @Data
    public static class SubButton extends AbstractButton{

        /**
         * 二级菜单
         */
        private List<AbstractButton> subButton;

        public SubButton(String name, List<AbstractButton> subButton) {
            super(name);
            this.subButton = subButton;
        }
    }

    /**
     * 类描述：
     * click 按钮
     * @author cfl
     * @date 2023/3/16 20:16
     * @version 1.0
     */
    @Data
    public static class ClickButton extends AbstractButton {
        private String type = "click";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        public ClickButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }
    
    /**
     * 类描述：
     * view 类型按钮
     * @author cfl 
     * @date 2023/3/16 20:17 
     * @version 1.0
     */
    @Data
    public static class ViewButton extends AbstractButton {
        private String type = "view";

        /**
         * 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为 miniprogram 时，不支持小程序的老版本客户端将打开本url。
         */
        private String url;

        public ViewButton(String name, String url) {
            super(name);
            this.url = url;
        }
    }

    /**
     * 类描述：
     * scancode_push 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class ScancodePushButton extends AbstractButton {
        private String type = "scancode_push";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * 下级菜单为空
         */
        private List<AbstractButton> subButton = new ArrayList<>(0);

        public ScancodePushButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * scancode_waitmsg 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class ScancodeWaitmsgButton extends AbstractButton {
        private String type = "scancode_waitmsg";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * 下级菜单为空
         */
        private List<AbstractButton> subButton = new ArrayList<>(0);

        public ScancodeWaitmsgButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * pic_sysphoto 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class PicSysphotoButton extends AbstractButton {
        private String type = "pic_sysphoto";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * 下级菜单为空
         */
        private List<AbstractButton> subButton = new ArrayList<>(0);

        public PicSysphotoButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * pic_photo_or_album 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class PicPhotoOrAlbumButton extends AbstractButton {
        private String type = "pic_photo_or_album";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * 下级菜单为空
         */
        private List<AbstractButton> subButton = new ArrayList<>(0);

        public PicPhotoOrAlbumButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * pic_weixin 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class PicWeixinButton extends AbstractButton {
        private String type = "pic_weixin";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * 下级菜单为空
         */
        private List<AbstractButton> subButton = new ArrayList<>(0);

        public PicWeixinButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * location_select 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class LocationSelectButton extends AbstractButton {
        private String type = "location_select";

        /**
         * 菜单 KEY 值，用于消息接口推送，不超过128字节
         */
        private String key;

        public LocationSelectButton(String name, String key) {
            super(name);
            this.key = key;
        }
    }

    /**
     * 类描述：
     * media_id 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class MediaIdButton extends AbstractButton {
        private String type = "media_id";

        private String mediaId;

        public MediaIdButton(String name, String mediaId) {
            super(name);
            this.mediaId = mediaId;
        }
    }

    /**
     * 类描述：
     * article_id 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class ArticleIdButton extends AbstractButton {
        private String type = "article_id";

        private String articleId;

        public ArticleIdButton(String name, String articleId) {
            super(name);
            this.articleId = articleId;
        }
    }

    /**
     * 类描述：
     * article_view_limited 类型按钮
     * @author cfl
     * @date 2023/3/16 20:17
     * @version 1.0
     */
    @Data
    public static class ArticleViewLimitedButton extends AbstractButton {
        private String type = "article_view_limited";

        private String articleId;

        public ArticleViewLimitedButton(String name, String articleId) {
            super(name);
            this.articleId = articleId;
        }
    }
}
