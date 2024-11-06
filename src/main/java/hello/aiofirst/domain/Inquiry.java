package hello.aiofirst.domain;
import jakarta.persistence.*;
import lombok.*;


@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;
    private String password;
    private String title;
    private String content;
    private String img;
    private boolean answer;

    public void changeAnswer(boolean check){
        this.answer = check;

    }

}
