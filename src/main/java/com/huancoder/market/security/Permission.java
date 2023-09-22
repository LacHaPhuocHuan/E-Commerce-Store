package com.huancoder.market.security;


public enum Permission {
    admin_read("admin:read"),
    admin_change("admin:change"),
    admin_write("admin:write"),
    user_read("user:read"),
    user_write("user:write"),
    user_change("user:change");
    private String authority;

    Permission(String authority) {
        this.authority = authority;
    }


}
