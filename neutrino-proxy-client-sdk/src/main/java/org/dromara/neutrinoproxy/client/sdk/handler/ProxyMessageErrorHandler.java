package org.dromara.neutrinoproxy.client.sdk.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.dromara.neutrinoproxy.core.*;
import org.dromara.neutrinoproxy.core.dispatcher.Match;

/**
 * 异常信息处理器
 * @author: aoshiguchen
 * @date: 2022/6/16
 */
@Slf4j
@Match(type = Constants.ProxyDataTypeName.ERROR)
public class ProxyMessageErrorHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		log.info("error: {}", proxyMessage.getInfo());
        JSONObject load = JSONUtil.parseObj(proxyMessage.getInfo());
        Integer code = load.getInt("code");
		if (ExceptionEnum.AUTH_FAILED.getCode().equals(code)) {
			System.exit(0);
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.DISCONNECT.getDesc();
	}

}