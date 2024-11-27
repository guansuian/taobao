package org.example.taobao.dto;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class UserDto {
    private String username;

    private String password;

    private String captcha;

    private String email;

}
