package com.server.classload.packetloader.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.server.classload.PacketFactory;
import com.server.classload.packetloader.PacketLoader;
import com.server.classload.packetproto.PacketSet.NamePacket;
import io.netty.channel.ChannelHandlerContext;

public class NamePacketLoader extends PacketLoader {

    public NamePacketLoader () {
        packetBuilder       = NamePacket.newBuilder();
        relatedPacketName   = "NamePacket";
    }

    @Override
    public void loadDataToWorker(ChannelHandlerContext clientChannel, ByteString data) throws InvalidProtocolBufferException {
        NamePacket namePacket = NamePacket.parseFrom(data);
        System.out.println(namePacket);

        //test
        if (packetBuilder instanceof NamePacket.Builder) {
            namePacket      = ((NamePacket.Builder) packetBuilder).setName("Server Name Data").build();
            byte[] bytes    = PacketFactory.getInstance().buildBasePacket(namePacket);

            sendPacket(clientChannel, bytes);
        }
        //test
    }
}