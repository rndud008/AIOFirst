package hello.aiofirst.service;

import hello.aiofirst.domain.Order;
import hello.aiofirst.domain.OrderStatus;
import hello.aiofirst.dto.OrderDTO;
import hello.aiofirst.dto.OrderDetailDTO;
import hello.aiofirst.dto.OrderRequestDTO;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrderDTOList(boolean adminCheck);
    List<OrderDTO> getOrderDTOList(OrderStatus orderStatus);

    OrderDetailDTO getOrderDetailDTO(Long orderId);

    Order orderStatusChange(OrderRequestDTO orderRequestDTO);

}
