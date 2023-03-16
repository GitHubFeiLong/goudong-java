package com.goudong.wx.central.control.util;

import com.goudong.core.util.ListUtil;
import com.goudong.wx.central.control.dto.req.CreateMenuReq;
import com.goudong.wx.central.control.dto.resp.AccessTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static com.goudong.wx.central.control.dto.req.CreateMenuReq.*;

@Slf4j
@ExtendWith({})
class WxRequestUtilTest {

    @Test
    void getAccessToken() {
        AccessTokenResp resp = WxRequestUtil.getAccessToken("wx1948fa77ad11a423", "410d996604e66d6278d1e5a4d0f49e9c");
        log.info("{}", resp);
    }

    @Test
    void getStableAccessToken() {
    }

    @Test
    void createMenu() {
        String accessToken = "66_1xYS5csD-h0AV8bpgwToAbsIWWm6X_qrRXYYuFjD4tWjediy3Q0-IuxXefCu2j5MXiUh4ozmQvRNZZrg1dy5Jz1abGyc8ZpRjMFILkyFVX0M4ebx2X-WFBUUN2gQSIcAEAJLB";

        List<AbstractButton> buttons = new ArrayList<>();
        buttons.add(new ClickButton("今日歌曲", "V1001_TODAY_MUSIC"));

        AbstractButton btn2 = new SubButton("菜单", ListUtil.newArrayList(
                new ViewButton("搜索", "http://www.soso.com/"),
                new ClickButton("赞一下我们", "V1001_GOOD")
        ));
        buttons.add(btn2);

        AbstractButton btn3 = new SubButton("扫码", ListUtil.newArrayList(
                new ScancodeWaitmsgButton("扫码带提示", "rselfmenu_0_0"),
                new ScancodePushButton("扫码推事件", "rselfmenu_0_1")
        ));
        buttons.add(btn3);
        CreateMenuReq req = new CreateMenuReq(buttons);
        WxRequestUtil.createMenu(accessToken, req);
    }
}