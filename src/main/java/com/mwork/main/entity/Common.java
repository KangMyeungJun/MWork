package com.mwork.main.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter @Setter
public class Common {

    @Value("${domain}")
    private String domain;

}
