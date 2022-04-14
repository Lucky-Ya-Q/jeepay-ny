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
package com.jeequan.jeepay.pay.ctrl;

import com.jeequan.jeepay.core.cache.RedisUtil;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.WxUser;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.core.model.security.JeeWxUser;
import com.jeequan.jeepay.service.impl.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前登录者的信息相关接口
 *
 * @author terrfly
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
@RestController
@RequestMapping("/api/current")
public class CurrentUserController extends CommonCtrl {
    @Autowired
    private WxUserService wxUserService;

    @GetMapping("/user")
    public ApiRes currentUserInfo() {
        return ApiRes.ok(getCurrentUser());
    }

    @PutMapping("/user")
    public ApiRes modifyCurrentUserInfo() {
        String cacheKey = getCurrentUser().getCacheKey();
        Long currentUserId = getCurrentUser().getWxUser().getUserId();

        // 修改头像和昵称
        WxUser updateRecord = new WxUser();
        updateRecord.setUserId(currentUserId);
        updateRecord.setAvatar(getValString("avatar"));
        updateRecord.setNikename(getValString("nikename"));
        wxUserService.updateById(updateRecord);

        // 保存redis最新数据
        WxUser wxUser = wxUserService.getById(currentUserId);
        RedisUtil.set(cacheKey, new JeeWxUser(wxUser, cacheKey), CS.TOKEN_TIME);
        return ApiRes.ok();
    }
}
