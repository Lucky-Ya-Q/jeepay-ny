package com.jeequan.jeepay.core.model.security;

import com.jeequan.jeepay.core.entity.WxUser;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
public class JeeWxUser {
    /**
     * 微信用户信息
     **/
    private WxUser wxUser;

    /**
     * 缓存标志
     **/
    private String cacheKey;

    public JeeWxUser(WxUser wxUser, String cacheKey) {
        this.setWxUser(wxUser);
        this.setCacheKey(cacheKey);
    }
}
