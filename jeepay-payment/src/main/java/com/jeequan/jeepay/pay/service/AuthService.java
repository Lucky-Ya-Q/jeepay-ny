/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeequan.jeepay.pay.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.jeequan.jeepay.core.cache.RedisUtil;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.WxUser;
import com.jeequan.jeepay.core.jwt.JWTPayload;
import com.jeequan.jeepay.core.jwt.JWTUtils;
import com.jeequan.jeepay.core.model.security.JeeWxUser;
import com.jeequan.jeepay.pay.config.SystemYmlConfig;
import com.jeequan.jeepay.service.mapper.WxUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证Service
 *
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
@Slf4j
@Service
public class AuthService {
    @Autowired
    private SystemYmlConfig systemYmlConfig;
    @Autowired
    private WxUserMapper wxUserMapper;

    /**
     * 认证
     **/
    public String auth(WxMaJscode2SessionResult session) {
        String openid = session.getOpenid();
        String unionid = session.getUnionid();
        String sessionKey = session.getSessionKey();

        WxUser wxUser = wxUserMapper.selectOne(WxUser.gw().eq(WxUser::getOpenid, openid));
        if (wxUser == null) {
            wxUser = new WxUser();
            wxUser.setOpenid(openid);
            wxUser.setUnionid(unionid);
            wxUser.setSessionKey(sessionKey);
            wxUserMapper.insert(wxUser);
        } else {
            wxUser.setUnionid(unionid);
            wxUser.setSessionKey(sessionKey);
            wxUserMapper.updateById(wxUser);
        }

        // 生成token
        String cacheKey = CS.getCacheKeyToken(wxUser.getUserId(), IdUtil.fastUUID());
        // 生成iToken 并放置到缓存
        RedisUtil.set(cacheKey, new JeeWxUser(wxUser, cacheKey), CS.TOKEN_TIME);

        JWTPayload jwtPayload = new JWTPayload();
        jwtPayload.setSysUserId(wxUser.getUserId());
        jwtPayload.setCacheKey(cacheKey);

        // 返回JWTToken
        return JWTUtils.generateToken(jwtPayload, systemYmlConfig.getJwtSecret());
    }
}
