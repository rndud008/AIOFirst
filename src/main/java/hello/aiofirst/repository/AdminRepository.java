package hello.aiofirst.repository;

import hello.aiofirst.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    @Query("select a from Admin a left join fetch a.roles where a.username = :username")
    Admin getWithRoles(@Param("username") String username);
}
