/**
 * Copyright (c) 2022 aoshiguchen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fun.asgc.neutrino.proxy.server.controller;

import fun.asgc.neutrino.core.annotation.Autowired;
import fun.asgc.neutrino.core.annotation.NonIntercept;
import fun.asgc.neutrino.core.web.annotation.PostMapping;
import fun.asgc.neutrino.core.web.annotation.RequestBody;
import fun.asgc.neutrino.core.web.annotation.RequestMapping;
import fun.asgc.neutrino.core.web.annotation.RestController;
import fun.asgc.neutrino.proxy.server.controller.req.LoginReq;
import fun.asgc.neutrino.proxy.server.controller.res.LoginRes;
import fun.asgc.neutrino.proxy.server.dal.entity.UserDO;
import fun.asgc.neutrino.proxy.server.dal.entity.UserTokenDO;
import fun.asgc.neutrino.proxy.server.service.UserService;
import fun.asgc.neutrino.proxy.server.util.Md5Util;
import fun.asgc.neutrino.proxy.server.util.ParamCheckUtil;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author: aoshiguchen
 * @date: 2022/7/31
 */
@NonIntercept
@RequestMapping
@RestController
public class IndexController {
	@Autowired
	private UserService userService;

	@PostMapping("login")
	public LoginRes login(@RequestBody LoginReq req) {
		ParamCheckUtil.checkNotEmpty(req.getLoginName(), "loginName");
		ParamCheckUtil.checkNotEmpty(req.getLoginPassword(), "loginPassword");

		UserDO userDO = userService.findByLoginName(req.getLoginName());
		if (null == userDO || !Md5Util.encode(req.getLoginPassword()).equals(userDO.getLoginPassword())) {
			// TODO 抛出异常
		}
		String token = UUID.randomUUID().toString().replaceAll("-", "");

		Date now = new Date();
		// TODO 计算过期时间
		userService.addUserToken(new UserTokenDO()
			.setToken(token)
			.setUserId(userDO.getId())
			.setExpirationTime(now)
			.setCreateTime(now)
			.setUpdateTime(now)
		);

		return new LoginRes()
			.setToken(token)
			.setUserId(userDO.getId())
			.setUserName(userDO.getName());
	}

}