package com.swagger.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private String message;
    private Object data;
    private Integer statusCode;

}
