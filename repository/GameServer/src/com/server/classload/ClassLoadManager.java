package com.server.classload;

import com.conf.ClassLoadConfig;
import com.server.classload.packetloader.PacketLoader;
import com.server.classload.packetproto.PacketSet;
import com.util.BaseUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manager for class loading.
 */
public class ClassLoadManager {
    private static final Logger LOG = Logger.getLogger(ClassLoadManager.class.getName());

    /**
     * Load class instances to cache of packet factory.
     */
    public void load() {
        try {
//            find(ClassLoadConfig.LOADER_PATH);
            cacheLoaders(ClassLoadConfig.LOADER_PATH);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString(), e.getMessage());
        }
    }

    private void cacheLoaders(String loadPath) throws Exception {
        PacketFactory.getInstance().cachePacketLoaderInstances(
                BaseUtil.getLoaderInstancesFromPackage(loadPath)
        );
    }

    private void find(String loadPath) throws Exception {
        //load loaders
//        long l1 = System.nanoTime();
        List<PacketLoader> loaders = BaseUtil.getLoaderInstancesFromPackage(loadPath);
//        l1 = System.nanoTime() - l1;
//        System.out.println("load loaders - " + l1);

        //cash loaders
//        l1 = System.nanoTime();
        PacketFactory.getInstance().cachePacketLoaderInstances(loaders);
//        l1 = System.nanoTime() - l1;
//        System.out.println("cash loaders - " + l1);

//        //usual packet
//        l1 = System.nanoTime();
        PacketSet.NamePacket namePacket = PacketSet.NamePacket.newBuilder().setName("ASDSF").build();
//        l1 = System.nanoTime() - l1;
//        System.out.println("usual packet - " + l1);
//
//        //build packet      23482458
//        l1 = System.nanoTime();
//        byte[] bytes = PacketFactory.getInstance().buildBasePacket(namePacket);
//        l1 = System.nanoTime() - l1;
//        System.out.println("build packet - " + l1);
//
//        //parse base packet
//        l1 = System.nanoTime();
//        PacketSet.BasePacket basePacket = PacketSet.BasePacket.parseFrom(bytes);
//        l1 = System.nanoTime() - l1;
//        System.out.println("parse base   - " + l1);
//
//        //parse packet
//        l1 = System.nanoTime();
//        PacketFactory.getInstance().parseBasePacket(basePacket);
//        l1 = System.nanoTime() - l1;
//        System.out.println("parse inner  - " + l1);
    }

    public static void main(String[] args) throws Exception {
        ClassLoadManager m = new ClassLoadManager();
        m.load();
    }
}