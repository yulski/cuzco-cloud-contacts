package com.yulski.cuzco.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int userId;
    private String email;
    private String password;
}
