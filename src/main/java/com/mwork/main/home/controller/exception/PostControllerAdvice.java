package com.mwork.main.home.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class PostControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public String noSuchElementHandler(NoSuchElementException e) {
        e.printStackTrace();
        return "redirect:/post/pages";

    }


}
