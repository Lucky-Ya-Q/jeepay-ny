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
package com.jeequan.jeepay.mch.ctrl.anon;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.cache.RedisUtil;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.jwt.JWTPayload;
import com.jeequan.jeepay.core.jwt.JWTUtils;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
import com.jeequan.jeepay.mch.service.AuthService;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录鉴权
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
@RestController
@RequestMapping("/api/anon/auth")
public class AuthController extends CommonCtrl {

	@Autowired private AuthService authService;

	/** 用户信息认证 获取iToken  **/
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	@MethodLog(remark = "登录认证")
	public ApiRes validate() throws BizException {


		String account = Base64.decodeStr(getValStringRequired("ia"));  //用户名 i account, 已做base64处理
		String ipassport = Base64.decodeStr(getValStringRequired("ip"));	//密码 i passport,  已做base64处理
        String vercode = Base64.decodeStr(getValStringRequired("vc"));	//验证码 vercode,  已做base64处理
        String vercodeToken = Base64.decodeStr(getValStringRequired("vt"));	//验证码token, vercode token ,  已做base64处理

        String cacheCode = RedisUtil.getString(CS.getCacheKeyImgCode(vercodeToken));
        if(StringUtils.isEmpty(cacheCode) || !cacheCode.equalsIgnoreCase(vercode)){
            throw new BizException("验证码有误！");
        }

		// 返回前端 accessToken
		String accessToken = authService.auth(account, ipassport);

        // 删除图形验证码缓存数据
        RedisUtil.del(CS.getCacheKeyImgCode(vercodeToken));

		return ApiRes.ok4newJson(CS.ACCESS_TOKEN_NAME, accessToken);
	}

	/** 图片验证码  **/
	@RequestMapping(value = "/vercode", method = RequestMethod.GET)
	public ApiRes vercode() throws BizException {

		SpecCaptcha specCaptcha = new SpecCaptcha(137, 40, 4);

        //redis
		String vercodeToken = UUID.fastUUID().toString();
        RedisUtil.setString(CS.getCacheKeyImgCode(vercodeToken), specCaptcha.text(), CS.VERCODE_CACHE_TIME ); //图片验证码缓存时间: 1分钟

        JSONObject result = new JSONObject();
        result.put("imageBase64Data", specCaptcha.toBase64());
        result.put("vercodeToken", vercodeToken);
		result.put("expireTime", CS.VERCODE_CACHE_TIME);

		return ApiRes.ok(result);
	}

	@PostMapping("/codeLogin")
	@MethodLog(remark = "授权码登录")
	public ApiRes codeLogin() {
		String authToken = getValStringRequired(CS.CODE);

		JWTPayload jwtPayload = JWTUtils.parseToken(authToken, CS.JWT_SECRET);
		if (jwtPayload == null) {
			return ApiRes.customFail("授权码认证失败！");
		}

		long date = System.currentTimeMillis() - jwtPayload.getCreated();
		if (date > (2 * 60 * 1000)) { // 两分钟内有效
			return ApiRes.customFail("授权码过期！");
		}

		String accessToken = authService.auth(jwtPayload);
		return ApiRes.ok4newJson(CS.ACCESS_TOKEN_NAME, accessToken);
	}

}
