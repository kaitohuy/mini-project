package com.nhom8.mini_project;

import java.io.Serializable;

// Implement Serializable để dễ dàng truyền object qua lại giữa các Activity
public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isAvailable; // true = Còn trống, false = Đã thuê
    private String tenantName;
    private String phone;

    public Room(String id, String name, double price, boolean isAvailable, String tenantName, String phone) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.tenantName = tenantName;
        this.phone = phone;
    }

    // Các Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}