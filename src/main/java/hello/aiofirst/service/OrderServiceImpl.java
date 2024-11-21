package hello.aiofirst.service;

import hello.aiofirst.domain.*;
import hello.aiofirst.dto.*;
import hello.aiofirst.repository.OrderItemRepository;
import hello.aiofirst.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderDTOList(boolean adminCheck) {
        List<Order> orders;

        if (adminCheck) {
            orders = orderRepository.getOrderList(List.of(PaymentStatus.SUCCESS),adminCheck);
        } else {
            orders = orderRepository.getOrderList(List.of(PaymentStatus.SUCCESS, PaymentStatus.CANCEL));
        }

        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        long index = orders.size();
        for (Order order : orders) {
            long total = 0;
            List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

            for (OrderItem orderItem : orderItems) {
                total += getTotal(orderItem);
                orderItemDTOS.add(getOrderItemDTO(orderItem));
            }

            orderDTOS.add(getOrderDTO(order, total, index, orderItemDTOS));
            index--;
        }

        return orderDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderDTOList(OrderStatus orderStatus) {

        List<Order> orders = orderRepository.getOrderList(List.of(PaymentStatus.SUCCESS), orderStatus);

        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        long index = orders.size();
        for (Order order : orders) {
            long total = 0;
            List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

            for (OrderItem orderItem : orderItems) {
                total += getTotal(orderItem);
                orderItemDTOS.add(getOrderItemDTO(orderItem));
            }

            orderDTOS.add(getOrderDTO(order, total, index, orderItemDTOS));
            index--;
        }

        return orderDTOS;
    }

    @Override
    public OrderDetailDTO getOrderDetailDTO(Long orderId) {
        Order order = orderRepository.getOrder(orderId);

        if (order == null) {
            return null;
        }

        List<OrderItem> orderItems = orderItemRepository.getOrderItemList(orderId);

        long totalPrice = 0;
        List<OrderDetailProductDTO> orderDetailProductDTOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {

            OrderDetailProductDTO orderDetailProductDTO = OrderDetailProductDTO.builder()
                    .productName(orderItem.getProductVariant().getProduct().getProductName())
                    .option("색상 : " + orderItem.getProductVariant().getColor() + " | 사이즈 : " + orderItem.getProductVariant().getSize() + " | " + orderItem.getQuantity() + "개")
                    .build();

            orderDetailProductDTOS.add(orderDetailProductDTO);

            totalPrice += orderItem.getQuantity() * (orderItem.getProductVariant().getPrice() + orderItem.getProductVariant().getProduct().getSellPrice());

        }

        long delivery = 0;

        if (totalPrice < 50000) {
            delivery = 3000;
        }
        String phoneNumber = "";
        phoneNumber = getString(order.getAddress().getPhoneNumber1(), phoneNumber);
        phoneNumber = getString(order.getAddress().getPhoneNumber2(), phoneNumber);

        return OrderDetailDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .orderStatus(order.getOrderStatus().getDescription())
                .orderDetailProductDTOS(orderDetailProductDTOS)
                .deliveryName(order.getAddress().getNickname())
                .fullAddressName("우편번호 : " + order.getAddress().getZipcode() + " |" + order.getAddress().getAddressName() + " " + order.getAddress().getAddressNameDetail())
                .totalAndDeliveryPrice(String.format("%,d", totalPrice + delivery) + "원")
                .totalPrice(String.format("%,d", totalPrice) + "원")
                .deliveryPrice(String.format("%,d", delivery) + "원")
                .phoneNumber(phoneNumber)
                .build();
    }

    private static String getString(long phone, String phoneNumber) {
        int phoneLength = String.valueOf(phone).length();

        if (phoneLength >= 10) {
            String lastValue = String.valueOf(phone).substring(phoneLength - 4, phoneLength);

            phoneLength -= 4;
            String middleValue = String.valueOf(phone).substring(phoneLength - 4, phoneLength);

            phoneLength -= 4;
            String firstValue = String.valueOf(phone).substring(0, phoneLength);

            phoneNumber += phoneNumber.contains("-") ? " | " : "";

            phoneNumber += firstValue + "-" + middleValue + "-" + lastValue;

        }
        return phoneNumber;
    }

    @Override
    @Transactional
    public Order orderStatusChange(OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(orderRequestDTO.getOrderId()).orElse(new Order());

        if (OrderStatus.PREPARING_ITEM.equals(OrderStatus.valueOf(orderRequestDTO.getStatus()))) {
            order.changeStatus(OrderStatus.valueOf(orderRequestDTO.getStatus()));
        } else {
            order.changeStatus(OrderStatus.valueOf(orderRequestDTO.getStatus()));
            order.changeAdmin(false);
        }

        return order;
    }

    private static OrderDTO getOrderDTO(Order order, long total, long index, List<OrderItemDTO> orderItemDTOS) {
        if (total < 50000) {
            total += 3000;
        }
        boolean adminCheck = order.isAdminCheck() || !OrderStatus.ORDER_CANCELED.equals(order.getOrderStatus()) && !OrderStatus.PREPARING_ITEM.equals(order.getOrderStatus());

        OrderDTO orderDTO = OrderDTO.builder()
                .index(index)
                .orderId(order.getId())
                .status(order.getOrderStatus().getDescription())
                .totalPrice(String.format("%,d", total) + "원")
                .createdAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .orderItemDTOS(orderItemDTOS)
                .adminCheck(adminCheck)
                .itemPending(order.getOrderStatus().name().equals(OrderStatus.PREPARING_ITEM.name()))
                .build();
        return orderDTO;
    }

    private static OrderItemDTO getOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .orderItemId(orderItem.getId())
                .productName(orderItem.getProductVariant().getProduct().getProductName())
                .option("색상: " + orderItem.getProductVariant().getColor() + " | 사이즈: " + orderItem.getProductVariant().getSize() + " | " + orderItem.getQuantity() + "개")
                .build();
        return orderItemDTO;
    }

    private static int getTotal(OrderItem orderItem) {
        return orderItem.getQuantity() * (orderItem.getProductVariant().getPrice() + orderItem.getProductVariant().getProduct().getSellPrice());
    }

}
