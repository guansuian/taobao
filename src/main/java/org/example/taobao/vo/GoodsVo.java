package org.example.taobao.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 关岁安
 */
@Data
public class GoodsVo {
    private String name;
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private Integer shopId;
    private String head;
    private Integer onePrice;
    private Integer synthesis;
    private LocalDateTime createTime;
    private Integer isMao;
}
