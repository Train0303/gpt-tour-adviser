package com.gptTour.backEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String result;
    private String msg;
    private Object data;
}
