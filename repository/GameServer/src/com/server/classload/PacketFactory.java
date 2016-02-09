package com.server.classload;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.server.classload.packetloader.PacketLoader;
import com.server.classload.packetproto.PacketSet.BasePacket;
import com.util.BaseUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for build/parse BasePacket from/to inner data packet.
 * Using two maps for creating relation between packet and loader by unique id.
 * One for writing(writePacketNameAndId) - create unique id for packet name.
 * Another for reading(readIdAndLoaderInstance) - use this unique id for loader instance.
 */
public class PacketFactory {
    private static final Logger LOG = Logger.getLogger(PacketFactory.class.getName());
    private static PacketFactory INSTANCE                       = new PacketFactory();
    private Map<String, Integer> writePacketNameAndId           = new HashMap<>();
    private Map<Integer, PacketLoader> readIdAndLoaderInstance  = new HashMap<>();
    private BasePacket.Builder builder                          = BasePacket.newBuilder();

    /**
     * Get ready instance of factory.
     * @return ready instance.
     */
    public static PacketFactory getInstance() {
        return INSTANCE;
    }

    private PacketFactory() {}

    /**
     * Cache instances of packet loaders as map with related packet name and loader instance.
     * @param packetLoaderInstances instances of loaders.
     */
    public void cachePacketLoaderInstances(List<PacketLoader> packetLoaderInstances) {
        for (PacketLoader packetLoaderInstance : packetLoaderInstances) {
            String relatedPacketName    = packetLoaderInstance.getRelatedPacketName();
            int uniqueIdForPacket       = BaseUtil.getUniqueIdForClassName(relatedPacketName);

            writePacketNameAndId.put(relatedPacketName, uniqueIdForPacket);
            readIdAndLoaderInstance.put(uniqueIdForPacket, packetLoaderInstance);
        }
    }

    /**
     * Parse base packet to concrete data packet and give it to loader.
     * @param basePacket wrapper/base packet for inner packet.
     * @throws InvalidProtocolBufferException
     */
    public void parseBasePacket(ChannelHandlerContext clientChannel, BasePacket basePacket) throws InvalidProtocolBufferException {
        int packetId                = basePacket.getPacketId();
        PacketLoader packetLoader   = readIdAndLoaderInstance.get(packetId);

        if (packetLoader != null) {
            packetLoader.loadDataToWorker(clientChannel, basePacket.getPacketData());
        } else {
            LOG.log(Level.WARNING, "Wrong packet id - " + packetId);
        }
    }

    /**
     * Get concrete data packet, wrap it by base packet and translate it in to bytes.
     * @param innerPacket concrete data packet.
     * @return byte presentation of base packet.
     */
    public byte[] buildBasePacket(Object innerPacket) {
        if (innerPacket instanceof AbstractMessageLite) {
            String innerPacketName = innerPacket.getClass().getSimpleName();

            if (writePacketNameAndId.containsKey(innerPacketName)) {
                AbstractMessageLite amInnerPacket   = (AbstractMessageLite) innerPacket;
                int idLoader                        = writePacketNameAndId.get(innerPacketName);

                BasePacket basePacket = builder.
                        setPacketId(idLoader).
                        setPacketData(amInnerPacket.toByteString()).build();

                return basePacket.toByteArray();
            }
        }
        return null;
    }
}