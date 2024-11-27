package org.example.taobao.dto;

import lombok.Data;
import org.example.taobao.pojo.BasicAttribute;

/**
 * @author 关岁安
 */
@Data
public class SpuDto {
    private Integer categoryId;
    private String spuName;
    private String title;
    private BasicAttribute[] basicAttributes;
    private Integer shoppingId;
    private Integer is;
}
