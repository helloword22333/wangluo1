package com.hwk.qqclient.service;





import com.hwk.qqcommon.Message;
import com.hwk.qqcommon.MessageType;

import java.io.*;

public class FileClientService {
    public void sendFiletoOne(String src,String dest,String senderId,String getterId) throws IOException {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSource(src);
        message.setDest(dest);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(src);
            byte[] bytes = new byte[(int) (new File(src).length())];
            fileInputStream.read(bytes);
            message.setFileByte(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ObjectOutputStream oos =new ObjectOutputStream(ManageClientConnectServe.getclientContentServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);
        System.out.println(message.getSender()+"发送"+message.getGetter()+"文件"+message.getSource());
    }
}
