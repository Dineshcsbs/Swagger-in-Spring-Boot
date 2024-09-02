package com.swagger.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponseDTO {

    private String token;
    private Date expiresIn;
}