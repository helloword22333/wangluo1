package com.hwk.qqclient.service;


import java.util.concurrent.ConcurrentHashMap;

//线程管理，是一个ConcurrentHashMap,线程安全
public class ManageClientConnectServe {
    private static ConcurrentHashMap<String ,clientContentServerThread> hm = new ConcurrentHashMap<>();
    public static void addClentConnetServierThread(String userId, clientContentServerThread clientContentServerThread){
        hm.put(userId,clientContentServerThread);
    }
    public static clientContentServerThread getclientContentServerThread(String userId){
        return hm.get(userId);
    }
}
