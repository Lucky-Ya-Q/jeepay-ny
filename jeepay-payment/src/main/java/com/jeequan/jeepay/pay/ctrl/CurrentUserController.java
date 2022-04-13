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

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.cache.ITokenService;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.SysEntitlement;
import com.jeequan.jeepay.core.entity.SysUser;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.core.model.security.JeeUserDetails;
import com.jeequan.jeepay.core.utils.TreeDataBuilder;
import com.jeequan.jeepay.service.impl.SysEntitlementService;
import com.jeequan.jeepay.service.impl.SysUserAuthService;
import com.jeequan.jeepay.service.impl.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 当前登录者的信息相关接口
 *
 * @author terrfly
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
@RestController
@RequestMapping("api/current")
public class CurrentUserController extends CommonCtrl{

	@Autowired private SysEntitlementService sysEntitlementService;
	@Autowired private SysUserService sysUserService;
	@Autowired private SysUserAuthService sysUserAuthService;

	@RequestMapping(value="/user", method = RequestMethod.GET)
	public ApiRes currentUserInfo() {

		///当前用户信息
		JeeUserDetails jeeUserDetails = getCurrentUser();
		SysUser user = jeeUserDetails.getSysUser();

		//1. 当前用户所有权限ID集合
		List<String> entIdList = new ArrayList<>();
		jeeUserDetails.getAuthorities().stream().forEach(r->entIdList.add(r.getAuthority()));

		List<SysEntitlement> allMenuList = new ArrayList<>();    //所有菜单集合

		//2. 查询出用户所有菜单集合 (包含左侧显示菜单 和 其他类型菜单 )
		if(!entIdList.isEmpty()){
			allMenuList = sysEntitlementService.list(SysEntitlement.gw()
					.in(SysEntitlement::getEntId, entIdList)
					.in(SysEntitlement::getEntType, Arrays.asList(CS.ENT_TYPE.MENU_LEFT, CS.ENT_TYPE.MENU_OTHER))
					.eq(SysEntitlement::getSysType, CS.SYS_TYPE.MCH)
					.eq(SysEntitlement::getState, CS.PUB_USABLE));
		}

		//4. 转换为json树状结构
		JSONArray jsonArray = (JSONArray) JSON.toJSON(allMenuList);
		List<JSONObject> allMenuRouteTree = new TreeDataBuilder(jsonArray,
				"entId", "pid", "children", "entSort", true)
				.buildTreeObject();

		//1. 所有权限ID集合
		user.addExt("entIdList", entIdList);
		user.addExt("allMenuRouteTree", allMenuRouteTree);

		return ApiRes.ok(getCurrentUser().getSysUser());
	}
}
