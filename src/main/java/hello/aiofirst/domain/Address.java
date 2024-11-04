package hello.aiofirst.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus;

    private String addressName;
    private String zipcode;
    private String addressNameDetail;

    private String nickname;
    private String orderMessage;
    private String bankTransferDepositor;// 무통장입금자

    private long phoneNumber1;
    private long phoneNumber2;
}
