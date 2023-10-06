package com.gptTour.backEnd.exception;

import com.gptTour.backEnd.dto.ResponseDto;
import com.gptTour.backEnd.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomErrorException(CustomException exception) {
        log.error("throw customException : {}", exception.getErrorCode());
        ResponseDto restApiException = new ResponseDto(exception.getErrorCode().getHttpStatus().value(), exception.getErrorCode().getHttpStatus().name(), exception.getErrorCode().getMessage(), "");
        return new ResponseEntity<>(restApiException, exception.getErrorCode().getHttpStatus());
    }
}