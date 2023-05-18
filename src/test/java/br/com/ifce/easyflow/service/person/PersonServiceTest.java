package br.com.ifce.easyflow.service.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.ifce.easyflow.controller.dto.person.PersonCreateDTO;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.service.PersonService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PersonServiceTest  {

    @Autowired
    private PersonService personService;

    @Test
    void findAll_User_WhenSuccessful(){

    }

    @Test
    void save_PersonCreateDTO_WhenSuccessful() {
        PersonCreateDTO personCreateDTO = createPersonCreateDTO();

        Person personSaved = personService.createPerson(personCreateDTO); 

        Assertions.assertNotNull(personCreateDTO.getId());
        Assertions.assertEquals(personCreateDTO.getName(), personSaved.getName());
        Assertions.assertEquals(personCreateDTO.getEmail(), personSaved.getEmail());
        Assertions.assertEquals(personCreateDTO.getPhone(), personSaved.getPhone());
        Assertions.assertEquals(personCreateDTO.getCourse_id(), personSaved.getCourse().getId());
        Assertions.assertEquals(personCreateDTO.getStudy_area_id(), personSaved.getStudy_area().getId());

    }


    private PersonCreateDTO createPersonCreateDTO() {
        return PersonCreateDTO.builder()
                .name("Marcos")
                .course_id(1L)
                .study_area_id(1L)
                .email("marcos@teste.com")
                .password("123456")
                .phone("8588406679")
                .build();
    }
    
}
