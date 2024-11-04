package hello.aiofirst.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public class Point extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private long point;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    public void pendingChange(PointStatus pointStatus){
        this.pointStatus = pointStatus;
    }

    public void cancelChange(PointStatus pointStatus ,long value){
        this.pointStatus = pointStatus;
    }



}
