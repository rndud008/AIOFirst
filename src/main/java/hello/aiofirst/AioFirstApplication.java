package hello.aiofirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AioFirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(AioFirstApplication.class, args);
    }

}
