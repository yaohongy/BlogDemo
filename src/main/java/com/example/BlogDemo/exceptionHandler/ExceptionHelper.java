package com.example.BlogDemo.exceptionHandler;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionHelper {

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class, FileSizeLimitExceededException.class})
    public String invalidInputException(Model model, Exception exception) {
        model.addAttribute("error", exception.getMessage());
        return "/404";
    }


}
