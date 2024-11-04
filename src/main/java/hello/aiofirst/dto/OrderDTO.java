package hello.aiofirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long index;
    private Long orderId;
    private List<OrderItemDTO> orderItemDTOS;
    private String totalPrice;
    private String status;
    private String createdAt;
    private boolean adminCheck;
    private boolean itemPending;
    private String buttonString;
}
