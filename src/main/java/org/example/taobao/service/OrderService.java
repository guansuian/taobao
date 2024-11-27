package org.example.taobao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.taobao.dto.DeleteAndReturnInventoryDto;
import org.example.taobao.dto.OrderDto;
import org.example.taobao.dto.OrderPageDto;
import org.example.taobao.vo.Result;

import java.util.List;

/**
 * @author 关岁安
 */
public interface OrderService {
    Result insertOrder(List<OrderDto> list) throws JsonProcessingException;

    Result gainOrderVoListByUserId(Integer userId);

    Result gainHasNoPayOrder(Integer userId, Integer page, Integer pageSize);

    Result deleteAndReturnInventory(DeleteAndReturnInventoryDto deleteAndReturnInventoryDto);

    Result gainHasPayOrder(OrderPageDto orderPageDto);

    Result insertInstantOrder(List<OrderDto> list);
}
