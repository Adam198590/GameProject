package com.conf;

public class NetworkConfig {
    public static final int PORT = 1024;

    public static final int MAIN_POOL_SIZE              = 1;
    public static final int CLIENT_POOL_SIZE            = 4;
    public static final long MAX_CHANNEL_MEMORY_SIZE    = 400000000;
    public static final long MAX_TOTAL_MEMORY_SIZE      = 2000000000;
    public static final int KEEP_ALIVE_TIME             = 60;

    public static final int BACK_LOG                = 500;
    public static final int CONNECT_TIMEOUT_MILLIS  = 10000;
}