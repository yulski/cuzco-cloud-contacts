package com.yulski.cuzco.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User extends Model {
    private int id;
    private String email;
    private String password;

    public static class Contract {
        public static final String TABLE_NAME = "users";
        public static final String ID_COLUMN = "user_id";
        public static final String EMAIL_COLUMN = "email";
        public static final String PASSWORD_COLUMN = "password";
    }
}
