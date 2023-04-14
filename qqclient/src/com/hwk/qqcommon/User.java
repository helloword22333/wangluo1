package com.hwk.qqcommon;

import java.io.Serializable;

/*
*@author hwk
* 表示一个用户信息
*/
public class User implements Serializable {
    private String userId;
    private String passwd;
    private static final long serialVersionUID = 1L;//提高兼容性
    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }
    public User(){

    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }


}
