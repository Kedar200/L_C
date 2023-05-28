package com.example.laptopcontroller.data;

public class Devices {
    private int id;
    private String Nick_name;
    private String Address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick_name() {
        return Nick_name;
    }

    public void setNick_name(String nick_name) {
        Nick_name = nick_name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Devices(int id, String nick_name, String address) {
        this.id = id;
        Nick_name = nick_name;
        Address = address;
    }

    public Devices(String nick_name, String address) {
        Nick_name = nick_name;
        Address = address;
    }

    public Devices() {
    }
}
