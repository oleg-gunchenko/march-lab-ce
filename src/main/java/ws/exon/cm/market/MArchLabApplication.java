package ws.exon.cm.market;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PreDestroy;

@SpringBootApplication
@RequiredArgsConstructor
public class MArchLabApplication {
    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(MArchLabApplication.class, args);
    }

    @PreDestroy
    public void shutdown() {
        ((ConfigurableApplicationContext) applicationContext).close();
        System.out.println("Shutting Down...............");
    }
}
