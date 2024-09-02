package com.swagger.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String state;
    private String city;
    private String zipcode;
    private String phoneNumber;
    private String email;
    private String password;
    private Boolean isEmployee;
    private String authorityId;
}