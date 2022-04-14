package com.jeequan.jeepay.mgr.ctrl.wxuser;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.entity.WxUser;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mgr.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wxUser")
public class WxUserController extends CommonCtrl {
    @Autowired
    private WxUserService wxUserService;

    @GetMapping
    public ApiRes list() {
        IPage<WxUser> pages = wxUserService.page(getIPage());
        return ApiRes.ok(pages);
    }
}
