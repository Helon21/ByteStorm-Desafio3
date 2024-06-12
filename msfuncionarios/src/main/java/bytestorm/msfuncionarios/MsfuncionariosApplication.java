package bytestorm.msfuncionarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsfuncionariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsfuncionariosApplication.class, args);
	}

}
