package com.server.classload.packetloader.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.server.classload.packetloader.PacketLoader;
import com.server.classload.packetproto.PacketSet.AgePacket;
import io.netty.channel.ChannelHandlerContext;

public class AgePacketLoader extends PacketLoader {

    @Override
    public String getRelatedPacketName() {
        return "AgePacket";
    }

    @Override
    public void loadDataToWorker(ChannelHandlerContext clientChannel, ByteString data) throws InvalidProtocolBufferException {
        AgePacket agePacket = AgePacket.parseFrom(data);
        System.out.println(agePacket);
    }
}