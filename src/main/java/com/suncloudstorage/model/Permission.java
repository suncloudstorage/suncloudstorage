package com.suncloudstorage.model;

public enum Permission {
    SUN_CLOUD_READ("cloud:read"),
    SUN_CLOUD_WRITE("cloud:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
