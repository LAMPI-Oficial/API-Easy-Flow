package br.com.ifce.easyflow.service.person;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.ifce.easyflow.controller.dto.person.PersonCreateDTO;
import br.com.ifce.easyflow.controller.dto.user.UserRequestDTO;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.UserRepository;
import br.com.ifce.easyflow.service.CourseService;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.StudyAreaService;
import br.com.ifce.easyflow.service.UserService;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ConflictException;

class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private StudyAreaService studyAreaService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    void findAll_Person_WhenSuccessful() {
        List<Person> persons = List.of(createPerson());
        PageImpl<Person> personsPage = new PageImpl<>(persons);

        when(personRepository.findAll()).thenReturn(persons);
        List<Person> all = personService.findAll();
        List<Person> personsSavedList = all.stream().toList();

        Assertions.assertFalse(personsSavedList.isEmpty());
        Assertions.assertEquals(persons.get(0).getEmail(), personsSavedList.get(0).getEmail());

        verify(personRepository).findAll();
    }

    @Test
    void findById_Person_WhenSuccessful(){
        Person person = createPerson();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        Person personSaved = personService.findById(1L);

        Assertions.assertEquals(person.getName(), personSaved.getName());
        Assertions.assertEquals(person.getEmail(), personSaved.getEmail());
        Assertions.assertEquals(person.getPhone(), personSaved.getPhone());
        Assertions.assertEquals(person.getCourse().getId(), personSaved.getCourse().getId());
        Assertions.assertEquals(person.getStudy_area().getId(), personSaved.getStudy_area().getId());
    }

    @Test
    void save_PersonCreateDTO_WhenSuccessful() {
        Course course = savedCourse();
        StudyArea studyArea = savedStudyArea();
        Person person = createPerson();
        PersonCreateDTO personPostDTO = PersonCreateDTO.builder()
                .name("Marcos")
                .course_id(course.getId())
                .study_area_id(studyArea.getId())
                .email("marcos@teste.com")
                .password("123456")
                .repeated_password("123456")
                .phone("8588406679")
                .build();

        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person personSaved = createPersonModel(personPostDTO);

        
        Assertions.assertEquals(person.getName(), personSaved.getName());
        Assertions.assertEquals(person.getEmail(), personSaved.getEmail());
        Assertions.assertEquals(person.getPhone(), personSaved.getPhone());
        Assertions.assertEquals(person.getCourse().getId(), personSaved.getCourse().getId());
        Assertions.assertEquals(person.getStudy_area().getId(), personSaved.getStudy_area().getId());
    }

    private Course savedCourse() {
        Course course = new Course();
        course.setName("Course-Teste");
        return course;
    }

    private StudyArea savedStudyArea() {
        StudyArea studyArea = new StudyArea();
        studyArea.setName("Back-end-Teste");
        return studyArea;
    }

    private Person createPerson() {
        return Person.builder()
                .name("Marcos")
                .course(savedCourse())
                .study_area(savedStudyArea())
                .email("marcos@teste.com")
                .phone("8588406679")
                .build();
    }

    private User savedUser(User userRequest){
        User newUser = new User();
        BeanUtils.copyProperties(userRequest, newUser);
        return userRepository.save(newUser);
    }

    public Person createPersonModel(PersonCreateDTO personCreateDTO) {
        User user = new User();

        user.setLogin(personCreateDTO.getEmail());
        user.setPassword(personCreateDTO.getPassword());
        Person person = new Person();
        BeanUtils.copyProperties(personCreateDTO, person);
        person.setUser(user);
        person.setCourse(savedCourse());
        person.setStudy_area(savedStudyArea());
        personService.save(person);
        user.setPerson(person);
        savedUser(user);
        return person;
    }
}
