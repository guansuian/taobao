package org.example.taobao.controller;

import org.example.taobao.dto.CommonOrderDto;
import org.example.taobao.dto.CommonPayOrderDto;
import org.example.taobao.dto.RefundDto;
import org.example.taobao.service.CommonOrderService;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 关岁安
 */

@RestController
@RequestMapping("/commonOrder")
public class CommonOrderController {

    @Autowired
    private CommonOrderService commonOrderService;

    @PostMapping("insertCommonOrder")
    public Result insertCommonOrder(@RequestBody List<CommonOrderDto> commonOrderDtoList){
        try {
            return commonOrderService.insertCommonOrderList(commonOrderDtoList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("库存异常");
        }
    }

    @PostMapping("gainCommonPayOrderList")
    Result gainCommonPayOrderList(@RequestBody CommonPayOrderDto commonPayOrderDto){
        return commonOrderService.gainCommonPayOrderList(commonPayOrderDto);
    }

    @PostMapping("refundCommonOrder")
    Result refundCommonOrder(@RequestBody RefundDto refundDto ){
        return commonOrderService.refundCommonOrder(refundDto);
    }

    @GetMapping("gainRefundCommonOrderList")
    public Result gainRefundCommonOrderList(@RequestParam Integer shoppingId){
        return commonOrderService.gainRefundCommonOrderList(shoppingId);
    }

    @GetMapping("cancelRefundCommonOrder")
    public Result cancelRefundCommonOrder(@RequestParam String orderId){
        return commonOrderService.cancelRefundCommonOrder(orderId);
    }

    @GetMapping("deleteCommonOrder")
    public Result deleteCommonOrder(@RequestParam String orderId){
        return commonOrderService.deleteCommonOrder(orderId);
    }

}
