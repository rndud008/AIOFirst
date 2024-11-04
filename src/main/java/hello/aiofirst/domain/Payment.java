package hello.aiofirst.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String tid;
    private Integer total_amount;
    private Integer tax_free_amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    public void changeStauts(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }


}
