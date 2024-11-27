package org.example.taobao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.taobao.dto.DeleteAndReturnInventoryDto;
import org.example.taobao.dto.OrderDto;
import org.example.taobao.dto.OrderPageDto;
import org.example.taobao.pojo.Order;
import org.example.taobao.service.OrderService;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/order")
public class OrderController {



    @Autowired
    private OrderService orderService;

    @PostMapping("insertOrder")
    public Result insertOrder(@RequestBody List<OrderDto> list) throws JsonProcessingException {
        return orderService.insertOrder(list);
    }

    @GetMapping("gainOrderVoListByUserId")
    public Result gainOrderVoListByUserId(@RequestParam Integer userId){
        return orderService.gainOrderVoListByUserId(userId);
    }

    @PostMapping("gainHasNoPayOrder")
    public Result gainHasNoPayOrder(@RequestBody OrderPageDto orderPageDto){
        Integer userId = orderPageDto.getUserId();
        Integer page = orderPageDto.getPage();
        Integer pageSize = orderPageDto.getPageSize();
        return orderService.gainHasNoPayOrder(userId,page,pageSize);
    }

    @PostMapping("gainHasPayOrder")
    public Result gainHasPayOrder(@RequestBody OrderPageDto orderPageDto){
        return orderService.gainHasPayOrder(orderPageDto);
    }



    @PostMapping("deleteAndReturnInventory")
    public Result deleteAndInventory(@RequestBody DeleteAndReturnInventoryDto deleteAndReturnInventoryDto){
        return orderService.deleteAndReturnInventory(deleteAndReturnInventoryDto);
    }

    @PostMapping("insertInstantOrder")
    public Result insertInstantOrder(@RequestBody List<OrderDto> list){
        return orderService.insertInstantOrder(list);
    }
}
