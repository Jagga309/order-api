package com.app.orderapi.mapper;

import com.app.orderapi.DTO.OrderDTO;
import com.app.orderapi.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(expression = "java(order.getAddress().getStreetNumber())",target = "address")
    @Mapping(expression = "java(order.getItem().getName())",target = "item")
    OrderDTO orderToOrderDTO(Order order);
}
