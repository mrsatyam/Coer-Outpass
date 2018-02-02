package com.example.hpm.Coer_e_Outpass;

/**
 * Created by hpm on 16-12-2017.
 */

public class Student {
    private String key;
    private long student_id;
    private String name;
    private long mobile;
    private String email;
    private String passwd;
    private String bhawan;
    private String gender;


    public Student() {
    }

    public Student(String key, long student_id, String name, long mobile, String email, String passwd, String bhawan, String gender) {
        this.key = key;
        this.student_id = student_id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.passwd = passwd;
        this.bhawan = bhawan;
        this.gender = gender;
    }

    public String getKey() {
        return key;
    }

    public long getStudent_id() {
        return student_id;
    }

    public String getName() {
        return name;
    }

    public long getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getBhawan() {
        return bhawan;
    }

    public String getGender() {
        return gender;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setBhawan(String bhawan) {
        this.bhawan = bhawan;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}