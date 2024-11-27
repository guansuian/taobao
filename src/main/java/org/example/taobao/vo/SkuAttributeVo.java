package org.example.taobao.vo;

import lombok.Data;
import org.example.taobao.pojo.SkuSonAttribute;

import java.util.List;

/**
 * @author 关岁安
 */
@Data
public class SkuAttributeVo {
    private Integer attributeId;
    private String name;
    private Integer categoryId;
    private List<SkuSonAttribute> options;
    private String head;
    public SkuAttributeVo(Integer attributeId, String name, Integer categoryId, List<SkuSonAttribute> options) {
        this.attributeId = attributeId;
        this.name = name;
        this.categoryId = categoryId;
        this.options = options;
    }
}
