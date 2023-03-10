package br.com.ifce.easyflow;

import br.com.ifce.easyflow.model.*;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;	


@SpringBootApplication
@EnableSwagger2
public class TemplateApplication implements CommandLineRunner {

	private final UserService userService;
	private final PersonService personService;


	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

	public TemplateApplication(UserService userService,PersonService personService){
		this.userService = userService;
		this.personService = personService;
	}

	@Override
	public void run(String... args) throws Exception {
		//DADOS PARA TESTE
		if(userService.search().isEmpty()){
			List<Person> persons = new ArrayList<>();

			

			for(Integer i = 0; i<20; i++){
				User user = new User();
				Course course = new Course();
				StudyArea study_area = new StudyArea();
				course.setName("ADS-"+i);
				study_area.setStudy_area_name("Backend-"+i);
				
				user.setLogin("user"+ i +"@teste.com.br");
				user.setPassword(new BCryptPasswordEncoder().encode("123456"));

				Person person = new Person();
				
				person.setEmail("user"+ i +"@teste.com.br");
				person.setName("Usuário "+ i);
				person.setUser(user);
				person.setCourse(course);
				person.setStudy_area(study_area);
				person = personService.save(person);
				persons.add(person);
			}
			
		}
	}
}
