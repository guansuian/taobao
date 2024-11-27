package org.example.taobao.vo;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class ShoppingVo {
    private Integer id;
    private Integer shopKeeper_id;
    private String name;
    private String type;
    private String head;
    private String address;
    private String username;
    private String pass;
    private String content;
    private String summary;
}
