package org.example.taobao.pojo;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class SkuSonAttribute {
    private String url;
    private String value;

    public SkuSonAttribute(String url, String value) {
        this.url = url;
        this.value = value;
    }
}
