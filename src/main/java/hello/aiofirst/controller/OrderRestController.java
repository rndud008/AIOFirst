package hello.aiofirst.controller;

import hello.aiofirst.domain.Order;
import hello.aiofirst.domain.OrderStatus;
import hello.aiofirst.dto.OrderDTO;
import hello.aiofirst.dto.OrderRequestDTO;
import hello.aiofirst.service.KakaoPayService;
import hello.aiofirst.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderRestController {

    private final OrderService orderService;
    private final KakaoPayService kakaoPayService;

    @PostMapping("/status")
    public ResponseEntity<?> orderStatusChange(@RequestBody OrderRequestDTO orderRequestDTO) {

        Order order = orderService.orderStatusChange(orderRequestDTO);

        if (order.getId() == null) {
            return ResponseEntity.status(404).body("FAIL");
        }

        if(orderRequestDTO.getStatus().equals("CANCEL")){
            kakaoPayService.kakaoCancel(order);
        }

        return ResponseEntity.status(200).body(order.getOrderStatus().getDescription());
    }

    @GetMapping("")
    public ResponseEntity<?> orderList(@RequestParam("status") String status) {
        List<OrderDTO> orderDTOS;
        if (status.isEmpty()) {
            orderDTOS = orderService.getOrderDTOList(false);
        } else if (status.equals("OK")) {
            orderDTOS = orderService.getOrderDTOList(OrderStatus.PREPARING_ITEM);
        } else if (status.equals("CANCEL")) {
            orderDTOS = orderService.getOrderDTOList(true);
        }else {
            return ResponseEntity.status(404).body("FAIL");
        }

        return ResponseEntity.status(200).body(orderDTOS);
    }
}
