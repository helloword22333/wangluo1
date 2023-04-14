package com.hwk.qqclient.view;

import com.hwk.qqclient.service.FileClientService;
import com.hwk.qqclient.service.UserClientService;
import com.hwk.qqclient.service.messageClient;
import com.hwk.qqclient.utils.utility;


public class view {
    private boolean loop =true;
    private String key = "";
    //定义用户工具成员
    private final   UserClientService userClientService = new UserClientService();
    //信息控制的
    private messageClient messageClient1 = new messageClient();
    //文件处理的
    private FileClientService fileClientService  = new FileClientService();
    public static void main(String[] args) throws Exception{
        new view().mainMenu();
    }
    //菜单
    private void mainMenu() throws Exception{
        while (loop){
            System.out.println("-------------欢迎登录网络通信系统-------------");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("输入你的需求：");
            key = utility.readString(1);
            switch(key){
                case "1":
                    System.out.println("输入用户号");
                    String userId = utility.readString(50);
                    System.out.println("输入密码");
                    String pwd = utility.readString(50);
                    //判断登陆是否成功
                    if(userClientService.checkedUser(userId,pwd)){
                        System.out.println("-------------欢迎"+userId+"登录网络通信系统-------------");
                        while (loop){
                            System.out.println("\n-------------欢迎"+userId+"进入二级菜单-------------");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群聊");
                            System.out.println("\t\t 3 私聊");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("输入你的选择：");
                            key = utility.readString(1);
                            switch (key){
                                case "1":
                                    //在线用户显示
                                    userClientService.onlineFreindList();
                                    break;
                                case "2":
                                    System.out.println("输入想对大家说的话：");
                                    String str = utility.readString(100);
                                    messageClient1.sendMessagetoAll(str,userId);
                                    //群聊方法
                                    break;
                                case "3":
                                    System.out.println("输入想私聊的用户名");
                                    String getterId = utility.readString(50);
                                    System.out.println("输入想说的话：");
                                    String content = utility.readString(100);
                                    //编写方法，发送请求给服务器，私聊
                                    messageClient1.sendMessagetoOne(content,userId,getterId);
                                    break;
                                case "4":
                                    System.out.println("输入目标用户");
                                    String getterId1 = utility.readString(50);
                                    System.out.println("输入你的文件地址(形式 d:\\\\xx.jpg)");
                                    String src = utility.readString(50);
                                    System.out.println("输入目标用户的文件地址");
                                    String dest = utility.readString(51);
                                   // messageClient1.
                                    fileClientService.sendFiletoOne(src,dest,userId,getterId1);
                                    break;
                                case "9":
                                    loop = false;
                                    //通知服务端退出系统
                                    userClientService.logout();
                                    break;
                            }
                        }
                    }
                    else{
                        System.out.println("-------------登录失败-------------");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }
}
