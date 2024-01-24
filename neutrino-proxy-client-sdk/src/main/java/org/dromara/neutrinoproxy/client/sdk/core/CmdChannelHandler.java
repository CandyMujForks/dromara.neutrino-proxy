package org.dromara.neutrinoproxy.client.sdk.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.dromara.neutrinoproxy.client.sdk.config.ProxyConfig;
import org.dromara.neutrinoproxy.client.sdk.handler.ProxyMessageFactory;
import org.dromara.neutrinoproxy.client.sdk.util.ProxyUtil;
import org.dromara.neutrinoproxy.core.Constants;
import org.dromara.neutrinoproxy.core.ProxyMessage;
import org.dromara.neutrinoproxy.core.dispatcher.Dispatcher;

/**
 * 处理与服务端之间的数据传输
 * @author: aoshiguchen
 * @date: 2022/6/16
 */
@Slf4j
public class CmdChannelHandler extends SimpleChannelInboundHandler<ProxyMessage>{
    private static volatile Boolean transferLogEnable = Boolean.FALSE;


    public CmdChannelHandler() {
        ProxyConfig proxyConfig = (ProxyConfig) ProxyMessageFactory.beanManager.get("proxyConfig").getBean();
        if (null != proxyConfig.getClient() && null != proxyConfig.getTunnel().getHeartbeatLogEnable()) {
            transferLogEnable = proxyConfig.getTunnel().getHeartbeatLogEnable();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage){
        if (ProxyMessage.TYPE_HEARTBEAT != proxyMessage.getType() || transferLogEnable) {
            log.debug("[CMD Channel]Client CmdChannel recieved proxy message, type is {}", proxyMessage.getType());
        }
//        beanHandler.getDispatcher().dispatch(ctx, proxyMessage);
        Dispatcher dispatcher = (Dispatcher) ProxyMessageFactory.beanManager.get("dispatcher").getBean();
        dispatcher.dispatch(ctx,proxyMessage);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (realServerChannel != null) {
            realServerChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[CMD Channel]Client CmdChannel disconnect");
        ProxyUtil.setCmdChannel(null);
        ProxyUtil.clearRealServerChannels();

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("[CMD Channel]Client CmdChannel Error channelId:{}", ctx.channel().id().asLongText(), cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            switch (event.state()) {
                case READER_IDLE:
                    // 读超时，断开连接
                    log.error("[CMD Channel] Read timeout disconnect");
                    ctx.channel().close();
                    break;
                case WRITER_IDLE:
                    ctx.channel().writeAndFlush(ProxyMessage.buildHeartbeatMessage());
                    break;
                case ALL_IDLE:
                    log.error("[CMD Channel] ReadWrite timeout disconnect");
                    ctx.close();
                    break;
            }
        }
    }

}
