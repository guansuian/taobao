package org.example.taobao.pojo;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class User {
    private String username;
    private Integer id;
    private String email;

    private String password;

    private String head;

    private String gender;

    public User(String username,String email,String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
