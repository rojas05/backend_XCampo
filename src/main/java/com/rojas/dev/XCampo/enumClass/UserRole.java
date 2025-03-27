package com.rojas.dev.XCampo.enumClass;

public enum UserRole {
    CLIENT,
    SELLER,
    DELIVERYMAN;

    public static UserRole fromString(String type) {
        for (UserRole userType : UserRole.values()) {
            if (userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }

        return null;
    }
}
