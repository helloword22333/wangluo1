package com.hwk.qqclient.service;

import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

//提供给消息相关的方法
public class messageClient {
    public void sendMessagetoOne(String content,String senderId,String getterId){
        Message mes = new Message();
        mes.setSender(senderId);
        mes.setContent(content);
        mes.setGetter(getterId);
        mes.setSendTime(new Date().toString());
        mes.setMesType(MessageType.MESSAGE_COMM_MES);

        System.out.println(senderId+"对"+getterId+"说:"+content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServe.getclientContentServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(mes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public void sendMessagetoAll(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_T0_ALL_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setSendTime(new Date().toString());
        System.out.println(senderId+"对大家说："+content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServe.getclientContentServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
