package hello.aiofirst.service;

import hello.aiofirst.domain.Order;
import hello.aiofirst.domain.OrderStatus;
import hello.aiofirst.domain.PaymentStatus;
import hello.aiofirst.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final OrderRepository orderRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderStatusChange(){

        List<Order> orders = orderRepository.getOrderList(PaymentStatus.SUCCESS,
                List.of(OrderStatus.PREPARING_ITEM_CHECK, OrderStatus.DELIVERY_IN_PROGRESS));

        if (!orders.isEmpty()){
            for (Order order : orders){
                if (order.getOrderStatus().equals(OrderStatus.PREPARING_ITEM_CHECK)){
                    order.changeStatus(OrderStatus.DELIVERY_IN_PROGRESS);
                } else if (order.getOrderStatus().equals(OrderStatus.DELIVERY_IN_PROGRESS)) {
                    order.changeStatus(OrderStatus.DELIVERY_COMPLETED);
                }
            }
        }


    }


}
