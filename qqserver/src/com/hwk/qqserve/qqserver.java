package com.hwk.qqserve;



import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;
import com.hwk.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
//服务器管理
public class qqserver {
    //提升成员变量，扩大作用域
    private ServerSocket serverSocket;
    //线程安全，比hashmap安全
    private static ConcurrentHashMap<String ,User> validUser = new ConcurrentHashMap<>(15);
    private static ConcurrentHashMap<String,ArrayList<Message>> messdb = new ConcurrentHashMap<>();
   // 离线信息的存储
    public static ConcurrentHashMap<String, ArrayList<Message>> getMessdb() {
        return qqserver.messdb;
    }

    public  static  ArrayList<Message> getlxList(String key1){
            return qqserver.messdb.get(key1);
    }
    public static void addMessdb(String key ,Message message) {
       ArrayList<Message> arrayList = new ArrayList<>();
       if(qqserver.getlxList(key) != null){
           arrayList = qqserver.getlxList(key);
       }
       arrayList.add(message);
       qqserver.messdb.put(key,arrayList);
    }
    //初始化
    static {
        validUser.put("100",new User("100","123"));
        validUser.put("hwk",new User("hwk","12345"));
        validUser.put("200",new User("200","123"));
        validUser.put("300",new User("300","123"));
        validUser.put("舞剑",new User("舞剑","123"));
        validUser.put("紫霞",new User("紫霞","321"));
    }
    //构造函数里执行
    public qqserver() {
        System.out.println("服务器在9990端口监听");
        try {
            serverSocket = new ServerSocket(9990);
            new Thread(new sendNewstoAll()).start();
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //接收客户端发的用户信息是否在服务端有记录
                User user1 = (User) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message mes = new Message();
                //有记录就放回MESSAGE_LONG_SUCCESS类型的mes
                if(checkUser(user1.getUserId(),user1.getPasswd())){
                    mes.setMesType(MessageType.MESSAGE_LONG_SUCCESS);
                    oos.writeObject(mes);
                    servserConnectClinetThread servserConnectClinetThread = new servserConnectClinetThread(socket, user1.getUserId());
                    servserConnectClinetThread.start();
                    ManageClientConnectServe.addClentConnetServierThread(user1.getUserId(),servserConnectClinetThread);

                    ArrayList<Message> arrayList = null;
                    if((arrayList = qqserver.getlxList(user1.getUserId())) != null){
                        for (Message mes1 : arrayList ) {
                            ObjectOutputStream oos1 =  new ObjectOutputStream(servserConnectClinetThread.getSocket().getOutputStream());
                            oos1.writeObject(mes1);
                            System.out.println(mes1.getSender()+"对"+mes1.getGetter()+"说："+mes1.getContent());
                        }
                        qqserver.messdb.remove(user1.getUserId());
                    }
                }
                //发送登录失败的记录
                else{
                    mes.setMesType(MessageType.MESSAGE_LONG_FAILE);
                    oos.writeObject(mes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //循环最后关闭serverSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //检测用户传的user是否在map里面
    public boolean checkUser(String userId,String pwd){
        User user = validUser.get(userId);
        if(user == null){
            return false;
        }
        if(!user.getPasswd().equals(pwd)){
            return false;
        }
        return true;
    }
}
