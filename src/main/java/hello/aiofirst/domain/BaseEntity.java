package hello.aiofirst.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}