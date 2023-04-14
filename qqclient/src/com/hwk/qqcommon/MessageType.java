package com.hwk.qqcommon;

public interface MessageType {
    String MESSAGE_LONG_SUCCESS = "1";//登陆成功的
    String MESSAGE_LONG_FAILE = "2";//登录失败的
    String MESSAGE_COMM_MES = "3";
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回在线用户
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户的列表
    String MESSAGE_CLIENT_EXIT = "6";//客户端退出
    String MESSAGE_T0_ALL_MES = "7";//群发的消息
    String MESSAGE_FILE_MES = "8";//文件的发送
}
