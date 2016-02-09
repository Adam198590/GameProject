package com.server.classload.packetloader;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import com.google.protobuf.GeneratedMessage.Builder;

/**
 * Class for working with packets and sending them.
 */
public abstract class PacketLoader {
    protected String relatedPacketName;
    protected Builder packetBuilder;

    /**
     * Get related packet name with this packet loader.
     * @return related packet name.
     */
    public String getRelatedPacketName() {
        return relatedPacketName;
    }

    /**
     * Parse packet from bytes and working with ready packet.
     * @param clientChannel client channel for send response, if it need.
     * @param data byte presentation of packet.
     * @throws InvalidProtocolBufferException
     */
    abstract public void loadDataToWorker(ChannelHandlerContext clientChannel, ByteString data)
            throws InvalidProtocolBufferException;

    /**
     * Send byte presentation of packet by client channel.
     * @param clientChannel channel/socket of client.
     * @param dataPacketBytes byte presentation of packet.
     */
    protected void sendPacket(ChannelHandlerContext clientChannel, byte[] dataPacketBytes) {
//        ByteBuf packetLength  = Unpooled.copyInt(dataPacketBytes.length);
        ByteBuf packetData    = Unpooled.copiedBuffer(dataPacketBytes);

//        clientChannel.write(packetLength);
        clientChannel.write(packetData);

        clientChannel.flush();
    }
}