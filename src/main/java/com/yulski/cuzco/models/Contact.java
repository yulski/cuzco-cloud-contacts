package com.yulski.cuzco.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private int contactId;
    private String name;
    private String phoneNumber;
    private int userId;
}
