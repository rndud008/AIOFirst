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
    public List<OrderDTO> getOrderDTOList() {
        List<Order> orders = orderRepository.getOrderList(List.of(PaymentStatus.SUCCESS, PaymentStatus.CANCEL));

        if (orders.isEmpty()){
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        long index = orders.size();
        for (Order order : orders){
            long total = 0;
            List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

            for(OrderItem orderItem : orderItems){
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

        if (orders.isEmpty()){
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();
        long index = orders.size();
        for (Order order : orders){
            long total = 0;
            List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

            for(OrderItem orderItem : orderItems){
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

        if(order == null){
            return null;
        }

        List<OrderItem> orderItems = orderItemRepository.getOrderItemList(orderId);

        long totalPrice = 0;
        List<OrderDetailProductDTO> orderDetailProductDTOS = new ArrayList<>();
        for(OrderItem orderItem : orderItems){

            OrderDetailProductDTO orderDetailProductDTO = OrderDetailProductDTO.builder()
                    .productName(orderItem.getProductVariant().getProduct().getProductName())
                    .option( "색상 : " +orderItem.getProductVariant().getColor() + " | 사이즈 : " + orderItem.getProductVariant().getSize() +" | " + orderItem.getQuantity() +"개")
                    .build();

            orderDetailProductDTOS.add(orderDetailProductDTO);

            totalPrice += orderItem.getQuantity() * (orderItem.getProductVariant().getPrice() + orderItem.getProductVariant().getProduct().getSellPrice());

        }

        long delivery = 0;

        if (totalPrice <50000){
            delivery = 3000;
        }
        String phoneNumber ="";
        phoneNumber = getString(order.getAddress().getPhoneNumber1(), phoneNumber);
        phoneNumber = getString(order.getAddress().getPhoneNumber2(), phoneNumber);

        return OrderDetailDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .orderStatus(order.getOrderStatus().getDescription())
                .orderDetailProductDTOS(orderDetailProductDTOS)
                .deliveryName(order.getAddress().getNickname())
                .fullAddressName( "우편번호 : " + order.getAddress().getZipcode() +" |" + order.getAddress().getAddressName() + " " + order.getAddress().getAddressNameDetail())
                .totalAndDeliveryPrice(String.format("%,d",totalPrice +delivery)+"원")
                .totalPrice(String.format("%,d",totalPrice))
                .deliveryPrice(String.format("%,d",delivery)+"원")
                .phoneNumber(phoneNumber)
                .build();
    }

    private static String getString(long phone,String phoneNumber) {
        int phoneLength = String.valueOf(phone).length();

        if (phoneLength >= 10){
            String lastValue = String.valueOf(phone).substring(phoneLength-4,phoneLength);

            phoneLength -=4;
            String middleValue = String.valueOf(phone).substring(phoneLength-4,phoneLength);

            phoneLength -=4;
            String firstValue = String.valueOf(phone).substring(phoneLength-4,phoneLength);

            phoneNumber += phoneNumber.contains("-") ? " | " : "";

            phoneNumber += firstValue + "-" + middleValue + "-" + lastValue;

        }
        return phoneNumber;
    }

    @Override
    @Transactional
    public Order orderStatusChange(OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(orderRequestDTO.getOrderId()).orElse(new Order());

        if (order.getId() == null && !orderRequestDTO.getStatus().equals("OK") && !orderRequestDTO.getStatus().equals("CANCEL")){
            return order;
        }

        if ( orderRequestDTO.getStatus().equals("OK")){
            order.changeStatus(OrderStatus.PREPARING_ITEM_CHECK);
        }else if (orderRequestDTO.getStatus().equals("CANCEL")){
            order.changeStatus(OrderStatus.ORDER_CANCELED);
        }

        return order;
    }

    private static OrderDTO getOrderDTO(Order order, long total, long index, List<OrderItemDTO> orderItemDTOS) {
        if (total < 50000){
            total += 3000;
        }

        OrderDTO orderDTO = OrderDTO.builder()
                .index(index)
                .orderId(order.getId())
                .status(order.getOrderStatus().getDescription())
                .totalPrice(String.format("%,d", total) + "원")
                .createdAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .orderItemDTOS(orderItemDTOS)
                .adminCheck(order.getOrderStatus().name().equals(OrderStatus.ADMIN_ITEM_CHECK.name()))
                .itemPending(order.getOrderStatus().name().equals(OrderStatus.PREPARING_ITEM.name()))
                .buttonString(getButtonString(order.getOrderStatus()))
                .build();
        return orderDTO;
    }
    private static String getButtonString(OrderStatus orderStatus){
        if (orderStatus.name().equals(OrderStatus.PREPARING_ITEM.name())){
            return "주문확인";
        }else if (orderStatus.name().equals(OrderStatus.ADMIN_ITEM_CHECK.name())){
            return "주문취소확인";
        }

        return "";
    }

    private static OrderItemDTO getOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .orderItemId(orderItem.getId())
                .productName(orderItem.getProductVariant().getProduct().getProductName())
                .option("색상: " + orderItem.getProductVariant().getColor() +" | 사이즈: " + orderItem.getProductVariant().getSize() + " | " + orderItem.getQuantity() + "개")
                .build();
        return orderItemDTO;
    }

    private static int getTotal(OrderItem orderItem) {
        return orderItem.getQuantity() * (orderItem.getProductVariant().getPrice() + orderItem.getProductVariant().getProduct().getSellPrice());
    }

}
