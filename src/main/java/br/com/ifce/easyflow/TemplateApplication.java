package br.com.ifce.easyflow;

import br.com.ifce.easyflow.model.*;
import br.com.ifce.easyflow.model.enums.StateEnum;
import br.com.ifce.easyflow.service.AddressService;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.ArrayList;
import java.util.List;	


@SpringBootApplication
@EnableSwagger2
public class TemplateApplication implements CommandLineRunner {

	private final UserService userService;
	private final PersonService personService;
	private final AddressService addressService;


	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

	public TemplateApplication(UserService userService,PersonService personService,AddressService addressService){
		this.userService = userService;
		this.personService = personService;
		this.addressService = addressService;
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
				study_area.setName("Backend-"+i);
				
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

				Address address = new Address();

				address.setComplement("uvasc");
				address.setMunicipality("knvs");
				address.setNeighborhood("sgsgdsd");
				address.setNumber("565");
				address.setStateEnum(StateEnum.CEARA);
				address.setStreet("dsdsgx");
				address.setPerson(person);
				address = addressService.save(address);

			}
			
		}
	}
}
