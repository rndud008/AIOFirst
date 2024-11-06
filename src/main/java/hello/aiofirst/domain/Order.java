package hello.aiofirst.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "m_order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String paymentOption;

    private String refundOption;
    private String refundMemberName;
    private String refundBankName;
    private String refundBankAccount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private boolean adminCheck;

    public void changeStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void changeAdmin(boolean check){
        this.adminCheck = check;
    }

}
