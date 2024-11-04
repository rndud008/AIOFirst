package hello.aiofirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO {
    private Long orderId;
    private String orderDate;
    private String orderStatus;
    private List<OrderDetailProductDTO> orderDetailProductDTOS;
    private String deliveryName;
    private String phoneNumber;
    private String fullAddressName;
    private String totalAndDeliveryPrice;
    private String totalPrice;
    private String deliveryPrice;

}
