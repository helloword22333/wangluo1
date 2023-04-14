package com.hwk.qqserve;


import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

//自定义线程
public class servserConnectClinetThread extends Thread{
    private Socket socket;
    private String userId;
    public servserConnectClinetThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }
    @Override
    public void run() {
        while (true){
            //不断循环执行
            System.out.println("服务器和客服端保持通信");
            try {
                //读取用户发的信息
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("服务器和客户端"+userId+"保持通信，读取数据中");
                Message mes = (Message) ois.readObject();
                //判断是否是请求用户列表的
                if(mes.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(mes.getSender()+"要在线用户的列表");
                    //放回用户列表
                    String onlineUser = ManageClientConnectServe.getOnlineUsers();
                    Message mes1 = new Message();
                    //十设置相关属性
                    mes1.setContent(onlineUser);
                    mes1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    mes1.setGetter(mes.getSender());
                    mes1.setSender(userId);
                    //发出
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(mes1);

                }
                else if(mes.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(mes.getSender()+"退出系统了");
                    ManageClientConnectServe.removeConnectClinetThread(mes.getSender());
                    socket.close();
                    break;
                }
                else if(mes.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    try {
                        servserConnectClinetThread servserConnectClinetThread = ManageClientConnectServe.getservserConnectClinetThread(mes.getGetter());
                        //看接收方是否登录
                        if(servserConnectClinetThread != null){
                            System.out.println(mes.getSender()+"对"+mes.getGetter()+"说:"+mes.getContent());
                            ObjectOutputStream oos =  new ObjectOutputStream(servserConnectClinetThread.getSocket().getOutputStream());
                            oos.writeObject(mes);
                        }
                        //离线信息的存储
                         else{
                            qqserver.addMessdb(mes.getGetter(),mes);
                        }
                    } catch (IOException e) {

                    }
                }
                else if(mes.getMesType().equals(MessageType.MESSAGE_T0_ALL_MES)){
                    String onlineName = "";
                    ConcurrentHashMap<String,servserConnectClinetThread> hm = ManageClientConnectServe.getHm();
                    Iterator<String> iterator = hm.keySet().stream().iterator();
                    while(iterator.hasNext()){
                        onlineName = iterator.next().toString();
                        if(!onlineName.equals(mes.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineName).getSocket().getOutputStream());
                            oos.writeObject(mes);
                        }
                    }
                    System.out.println(mes.getSender()+"对大家说:"+mes.getContent());
                }
                else if(mes.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServe.getservserConnectClinetThread(mes.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(mes);
                }
                else{
                    System.out.println("其他message,不做处理!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
