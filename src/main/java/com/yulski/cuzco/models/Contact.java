package com.yulski.cuzco.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact extends Model {
    private int id;
    private String name;
    private String phoneNumber;
    private User user;

    public boolean isValid() {
        return name != null &&
                name.length() > 0 &&
                user != null &&
                user.isValid();
    }

    public static class Contract {
        public static final String TABLE_NAME = "contacts";
        public static final String ID_COLUMN = "contact_id";
        public static final String NAME_COLUMN = "name";
        public static final String PHONE_NUMBER_COLUMN = "phone_number";
        public static final String USER_ID_COLUMN = "user_id";
    }
}
