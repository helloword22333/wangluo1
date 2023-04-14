package com.hwk.qqserve;

import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;
import com.hwk.utils.utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class sendNewstoAll implements  Runnable{
    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的新闻/消息 输入exit退出推送/quit退出服务器");
            String onlineUser = "";
            String str = utility.readString(50);
            if(str.equals("exit")){
                break;
            }
            if(str.equals("quit")){
                System.exit(0);
            }
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(str);
            message.setSendTime(new Date().toString());
            message.setMesType(MessageType.MESSAGE_T0_ALL_MES);
            System.out.println("服务器给所有人说" + message.getContent());
            //遍历所有线程
            ConcurrentHashMap<String, servserConnectClinetThread> hm = ManageClientConnectServe.getHm();
            Set<String> keySet = hm.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                onlineUser = iterator.next();
                try {
                    servserConnectClinetThread servserConnectClinetThread = hm.get(onlineUser);
                    ObjectOutputStream oos = new ObjectOutputStream(servserConnectClinetThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
