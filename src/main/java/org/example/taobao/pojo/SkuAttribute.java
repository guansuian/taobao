package org.example.taobao.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
public class SkuAttribute {
    private Integer attributeId;
    private String name;
    private Integer categoryId;
    private String options;
}
