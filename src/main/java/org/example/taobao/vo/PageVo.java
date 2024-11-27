package org.example.taobao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 关岁安
 * 分页查询之后的数据
 * @NoArgsConstructor 这个注解 是用来生成无参构造器
 * @AllArgsConstructor 这个注解 是用来生成全部参数的构造器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    private Long total;
    private List roes;
}
