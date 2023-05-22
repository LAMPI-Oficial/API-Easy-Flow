package br.com.ifce.easyflow.service.person;

import br.com.ifce.easyflow.controller.dto.person.PersonCreateDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.controller.dto.user.UserRequestDTO;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.service.CourseService;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.StudyAreaService;
import br.com.ifce.easyflow.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonUserServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private StudyAreaService studyAreaService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Person_WhenSuccessful() {
        Person person = createPerson();
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person savedPerson = personService.save(person);

        Assertions.assertEquals(person.getName(), savedPerson.getName());
        Assertions.assertEquals(person.getEmail(), savedPerson.getEmail());
        Assertions.assertEquals(person.getCourse(), savedPerson.getCourse());
        Assertions.assertEquals(person.getStudy_area(), savedPerson.getStudy_area());

        verify(personRepository).save(person);
    }

    @Test
    void createPerson_WhenSuccessful() {
        PersonCreateDTO personCreateDTO = createPersonCreateDTO();
        User user = createUser();
        Person person = createPerson();
        StudyArea studyArea = createStudyArea();
        Course course = createCourse();

        when(userService.save(any(UserRequestDTO.class))).thenReturn(user);
        when(courseService.searchByID(anyLong())).thenReturn(course);
        when(studyAreaService.searchByID(anyLong())).thenReturn(studyArea);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person createdPerson = personService.createPerson(personCreateDTO);

        Assertions.assertEquals(person.getName(), createdPerson.getName());
        Assertions.assertEquals(person.getEmail(), createdPerson.getEmail());
        Assertions.assertEquals(course.getId(), createdPerson.getCourse().getId());
        Assertions.assertEquals(studyArea.getId(), createdPerson.getStudy_area().getId());

        verify(personRepository).save(any(Person.class));
        verify(userService).save(any(UserRequestDTO.class));
        verify(courseService).searchByID(personCreateDTO.getCourse_id());
        verify(studyAreaService).searchByID(personCreateDTO.getStudy_area_id());
        
    }

    @Test
    void findAll_Persons_WhenSuccessful() {
        List<Person> persons = List.of(createPerson());
        when(personRepository.findAll()).thenReturn(persons);

        List<Person> allPersons = personService.findAll();

        Assertions.assertFalse(allPersons.isEmpty());
        Assertions.assertEquals(persons.get(0).getName(), allPersons.get(0).getName());

        verify(personRepository).findAll();
    }

    @Test
    void update_Person_WhenSuccessful() {
        Long id = 1L;
        PersonDTO personDTO = createPersonDTO();
        Person oldPerson = createPerson();
        StudyArea studyArea = createStudyArea();
        Course course = createCourse();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(oldPerson));
        when(studyAreaService.searchByID(anyLong())).thenReturn(studyArea);
        when(courseService.searchByID(anyLong())).thenReturn(course);
        when(personRepository.save(any(Person.class))).thenReturn(oldPerson);

        Person updatedPerson = personService.update(id, personDTO);

        Assertions.assertEquals(personDTO.getName(), updatedPerson.getName());
        Assertions.assertEquals(personDTO.getEmail(), updatedPerson.getEmail());
        Assertions.assertEquals(course, updatedPerson.getCourse());
        Assertions.assertEquals(studyArea, updatedPerson.getStudy_area());

        verify(personRepository).findById(id);
        verify(studyAreaService).searchByID(personDTO.getStudy_area_id());
        verify(courseService).searchByID(personDTO.getCourse_id());
        verify(personRepository).save(oldPerson);
    }

    @Test
    void delete_Person_WhenSuccessful() {
        Long id = 1L;
        Person person = createPerson();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        boolean result = personService.delete(id);

        Assertions.assertTrue(result);

        verify(personRepository).findById(id);
        verify(personRepository).delete(person);
    }

    @Test
    void findById_Person_WhenPersonExists() {
        Long id = 1L;
        Person person = createPerson();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        Person foundPerson = personService.findById(id);

        Assertions.assertEquals(person.getName(), foundPerson.getName());
        Assertions.assertEquals(person.getEmail(), foundPerson.getEmail());
        Assertions.assertEquals(person.getCourse(), foundPerson.getCourse());
        Assertions.assertEquals(person.getStudy_area(), foundPerson.getStudy_area());

        verify(personRepository).findById(id);
    }

    @Test
    void findById_Person_WhenPersonDoesNotExist() {
        Long id = 1L;

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            personService.findById(id);
        });

        verify(personRepository).findById(id);
    }

    @Test
    void existsById_WhenPersonExists() {
        Long id = 1L;

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(createPerson()));

        boolean result = personService.existsById(id);

        Assertions.assertTrue(result);

        verify(personRepository).findById(id);
    }

    @Test
    void existsById_WhenPersonDoesNotExist() {
        Long id = 1L;

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = personService.existsById(id);

        Assertions.assertFalse(result);

        verify(personRepository).findById(id);
    }

    @Test
    void existsByEmail_WhenEmailExists() {
        String email = "test@example.com";

        when(personRepository.findByEmail(any(String.class))).thenReturn(Optional.of(createPerson()));

        boolean result = personService.existsByEmail(email);

        Assertions.assertTrue(result);

        verify(personRepository).findByEmail(email);
    }

    @Test
    void existsByEmail_WhenEmailDoesNotExist() {
        String email = "test@example.com";

        when(personRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        boolean result = personService.existsByEmail(email);

        Assertions.assertFalse(result);

        verify(personRepository).findByEmail(email);
    }



    private Person createPerson() {
        Person person = new Person();
        person.setName("Marcola");
        person.setEmail("marcos@test.com.br");
        person.setCourse(createCourse());
        person.setStudy_area(createStudyArea());
        return person;
    }

    private PersonDTO createPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Marcola");
        personDTO.setEmail("marcos@test.com.br");
        personDTO.setCourse_id(createCourse().getId());
        personDTO.setStudy_area_id(createStudyArea().getId());
        return personDTO;
    }

    private PersonCreateDTO createPersonCreateDTO() {
        PersonCreateDTO personCreateDTO = new PersonCreateDTO();
        personCreateDTO.setName("Marcola");
        personCreateDTO.setEmail("marcos@test.com.br");
        personCreateDTO.setPassword("password");
        personCreateDTO.setRepeated_password("password");
        personCreateDTO.setCourse_id(createCourse().getId());
        personCreateDTO.setStudy_area_id(createStudyArea().getId());
        return personCreateDTO;
    }

    private User createUser() {
        User user = new User();
        user.setLogin("marcos@test.com.br");
        user.setPassword("password");
        return user;
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Computer Science");
        return course;
    }

    private StudyArea createStudyArea() {
        StudyArea studyArea = new StudyArea();
        studyArea.setId(1L);
        studyArea.setName("Software Engineering");
        return studyArea;
    }
}
