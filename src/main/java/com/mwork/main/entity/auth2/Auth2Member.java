package com.mwork.main.entity.auth2;

import com.mwork.main.entity.member.SocialType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth2Member {

    private String id;
    private String name;
    private String email;
    private SocialType socialType;


}
