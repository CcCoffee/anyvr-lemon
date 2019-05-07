package anyvr.app.lemon.handler;

import java.net.SocketAddress;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ChannelTest implements Channel {
    @Override public ChannelId id() {
        return null;
    }

    @Override public EventLoop eventLoop() {
        return null;
    }

    @Override public Channel parent() {
        return null;
    }

    @Override public ChannelConfig config() {
        return null;
    }

    @Override public boolean isOpen() {
        return false;
    }

    @Override public boolean isRegistered() {
        return false;
    }

    @Override public boolean isActive() {
        return false;
    }

    @Override public ChannelMetadata metadata() {
        return null;
    }

    @Override public SocketAddress localAddress() {
        return null;
    }

    @Override public SocketAddress remoteAddress() {
        return null;
    }

    @Override public ChannelFuture closeFuture() {
        return null;
    }

    @Override public boolean isWritable() {
        return false;
    }

    @Override public long bytesBeforeUnwritable() {
        return 0;
    }

    @Override public long bytesBeforeWritable() {
        return 0;
    }

    @Override public Unsafe unsafe() {
        return null;
    }

    @Override public ChannelPipeline pipeline() {
        return null;
    }

    @Override public ByteBufAllocator alloc() {
        return null;
    }

    @Override public ChannelFuture bind(final SocketAddress localAddress) {
        return null;
    }

    @Override public ChannelFuture connect(final SocketAddress remoteAddress) {
        return null;
    }

    @Override public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return null;
    }

    @Override public ChannelFuture disconnect() {
        return null;
    }

    @Override public ChannelFuture close() {
        return null;
    }

    @Override public ChannelFuture deregister() {
        return null;
    }

    @Override public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture disconnect(final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture close(final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture deregister(final ChannelPromise promise) {
        return null;
    }

    @Override public Channel read() {
        return null;
    }

    @Override public ChannelFuture write(final Object msg) {
        return null;
    }

    @Override public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        return null;
    }

    @Override public Channel flush() {
        return null;
    }

    @Override public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        return null;
    }

    @Override public ChannelFuture writeAndFlush(final Object msg) {
        return null;
    }

    @Override public ChannelPromise newPromise() {
        return null;
    }

    @Override public ChannelProgressivePromise newProgressivePromise() {
        return null;
    }

    @Override public ChannelFuture newSucceededFuture() {
        return null;
    }

    @Override public ChannelFuture newFailedFuture(final Throwable cause) {
        return null;
    }

    @Override public ChannelPromise voidPromise() {
        return null;
    }

    @Override public <T> Attribute<T> attr(final AttributeKey<T> key) {
        return null;
    }

    @Override public <T> boolean hasAttr(final AttributeKey<T> key) {
        return false;
    }

    @Override public int compareTo(final Channel o) {
        return 0;
    }
}
