package org.example.taobao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 关岁安
 */
@RestController
public class WebController {
    @GetMapping("/")
    public String get(){
        System.out.println("是否已经进来");
        return "关岁安";
    }
}
