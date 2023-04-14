package com.hwk.qqserve;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//服务端线程管理，包括socekt的属性
public class ManageClientConnectServe {
    private static ConcurrentHashMap<String ,servserConnectClinetThread> hm = new ConcurrentHashMap<>();
    //将线程加到map里面
    public static void addClentConnetServierThread(String userId, servserConnectClinetThread servserConnectClinetThread){
        hm.put(userId,servserConnectClinetThread);
    }
    //获得id线程
    public static servserConnectClinetThread getservserConnectClinetThread(String userId){
        return hm.get(userId);
    }
    //获得当前在线用户名
    public static String getOnlineUsers(){
        Set keySet = hm.keySet();
        Iterator<String> iterator = keySet.iterator();
        String onlineUserList = "";
        while (iterator.hasNext()){
            onlineUserList += iterator.next().toString()+" ";
        }
        return onlineUserList;
    }
    //
    public static void removeConnectClinetThread(String userid){
        hm.remove(userid);
    }

    public static ConcurrentHashMap<String, servserConnectClinetThread> getHm() {
        return hm;
    }
}
