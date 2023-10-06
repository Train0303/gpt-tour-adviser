package com.gptTour.backEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private int statusCode;
    private String result;
    private String msg;
    private Object data;
}
