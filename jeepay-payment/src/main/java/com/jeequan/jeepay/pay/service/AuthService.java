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

import cn.hutool.core.util.IdUtil;
import com.jeequan.jeepay.core.cache.ITokenService;
import com.jeequan.jeepay.core.cache.RedisUtil;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.MchInfo;
import com.jeequan.jeepay.core.entity.SysUser;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.exception.JeepayAuthenticationException;
import com.jeequan.jeepay.core.jwt.JWTPayload;
import com.jeequan.jeepay.core.jwt.JWTUtils;
import com.jeequan.jeepay.core.model.security.JeeUserDetails;
import com.jeequan.jeepay.pay.config.SystemYmlConfig;
import com.jeequan.jeepay.service.impl.MchInfoService;
import com.jeequan.jeepay.service.impl.SysRoleEntRelaService;
import com.jeequan.jeepay.service.impl.SysRoleService;
import com.jeequan.jeepay.service.impl.SysUserService;
import com.jeequan.jeepay.service.mapper.SysEntitlementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    @Autowired private SystemYmlConfig systemYmlConfig;

    /**
     * 认证
     * **/
    public String auth(String username, String password){

        JeeUserDetails jeeUserDetails = new JeeUserDetails();

        //验证通过后 再查询用户角色和权限信息集合

        SysUser sysUser = jeeUserDetails.getSysUser();



        //生成token
        String cacheKey = CS.getCacheKeyToken(sysUser.getSysUserId(), IdUtil.fastUUID());

        //生成iToken 并放置到缓存
        ITokenService.processTokenCache(jeeUserDetails, cacheKey); //处理token 缓存信息

        //将信息放置到Spring-security context中
        UsernamePasswordAuthenticationToken authenticationRest = new UsernamePasswordAuthenticationToken(jeeUserDetails, null, jeeUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationRest);

        //返回JWTToken
        return JWTUtils.generateToken(new JWTPayload(jeeUserDetails), systemYmlConfig.getJwtSecret());
    }
}
