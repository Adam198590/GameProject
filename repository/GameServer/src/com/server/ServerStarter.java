package com.server;

import com.conf.NetworkConfig;
import com.server.classload.ClassLoadManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ServerStarter {
    private static final Logger LOG = Logger.getLogger(ServerHandler.class.getName());

    public void start() {
        EventLoopGroup bossGroup            = new NioEventLoopGroup(NetworkConfig.MAIN_POOL_SIZE);
        EventLoopGroup workerGroup          = new NioEventLoopGroup(NetworkConfig.CLIENT_POOL_SIZE);
        ClassLoadManager classLoadManager   = new ClassLoadManager();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.SO_BACKLOG, 100);
            server.handler(new LoggingHandler(LogLevel.INFO));
            server.childHandler(new ServerInitializer());

            //cache packet loaders
            classLoadManager.load();

            // Start the server.
            ChannelFuture future = server.
                    bind(NetworkConfig.PORT).
                    sync();

            // Wait until the server socket is closed.
            future.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "exceptionCaught!!!", ex.getCause());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}