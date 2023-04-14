package com.hwk.qqclient.service;



import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
//用户线程，线程里有socket属性来联系服务
public class clientContentServerThread extends Thread{
    private Socket socket;
    public clientContentServerThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        System.out.println("客户端线程等待，从服务器端来的数据");
       while (true){
           try {
                ois = new ObjectInputStream(socket.getInputStream());
//               如果服务器没有发送Message对象，线程会阻塞在这里
               Message mes = (Message) ois.readObject();
               if(mes.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                   String[] onlienUser = mes.getContent().split(" ");
                   System.out.println("当前在线的用户列表");
                   for(int i = 0;i < onlienUser.length; i++){
                       System.out.println("用户"+onlienUser[i]);
                   }
               }
               else if(mes.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                   System.out.println("\n"+mes.getSender()+"对"+mes.getGetter()+"说:"+mes.getContent());
               }
               else if(mes.getMesType().equals(MessageType.MESSAGE_T0_ALL_MES)){
                   System.out.println("\n"+mes.getSender()+"对大家说:"+mes.getContent());
               }
               else if(mes.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                   System.out.println("\n"+mes.getSender()+"给"+mes.getGetter()+"发文件"+mes.getSource()+"到"+mes.getDest());
                   FileOutputStream fileOutputStream = new FileOutputStream(mes.getDest());
                   fileOutputStream.write(mes.getFileByte());
                   fileOutputStream.close();
                   System.out.println("保存文件成功");
               }
               else{
                   System.out.println("其他类型，不做处理的");
               }
           } catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
       }
    }
}
