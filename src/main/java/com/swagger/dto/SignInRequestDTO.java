package com.swagger.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRequestDTO {
    private String username;
    private String password;
}