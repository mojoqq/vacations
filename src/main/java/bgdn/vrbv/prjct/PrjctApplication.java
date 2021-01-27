package bgdn.vrbv.prjct;

import bgdn.vrbv.prjct.entity.Employee;
import bgdn.vrbv.prjct.entity.Role;
import bgdn.vrbv.prjct.repository.EmployeeRepository;
import bgdn.vrbv.prjct.service.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PrjctApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjctApplication.class, args);

	}

}
