package org.example.taobao.service;


import org.example.taobao.dto.CommonOrderDto;
import org.example.taobao.dto.CommonPayOrderDto;
import org.example.taobao.dto.RefundDto;
import org.example.taobao.vo.Result;

import java.util.List;

/**
 * @author 关岁安
 */
public interface CommonOrderService {
    Result insertCommonOrderList(List<CommonOrderDto> commonOrderDtoList);

    Result gainCommonPayOrderList(CommonPayOrderDto commonPayOrderDto);

    Result refundCommonOrder(RefundDto refundDto);

    Result gainRefundCommonOrderList(Integer shoppingId);

    Result cancelRefundCommonOrder(String orderId);

    Result deleteCommonOrder(String orderId);
}
