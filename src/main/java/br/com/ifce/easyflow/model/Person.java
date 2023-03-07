package br.com.ifce.easyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_cpf", unique = true)
    private String cpf;

    @Column(name = "person_phone")
    private String phone;

    @Column(name = "person_email")
    private String email;
    @JsonIgnore
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "person_is_visitor")
    private boolean isVisitor = true;

    @Column(name = "person_admin")
    private boolean isAdmin = false;

    public Person(Long id){
        this.id = id;
    }
    public String getFirtsName(){
        if(name != null){
            return name.split(" ")[0];
        }
        return "";
    }
        public String getLastName(){
        if(name != null){
            String[] separateNameBySpace = name.split(" ");

            return separateNameBySpace[separateNameBySpace.length-1];
        }
        return "";
    }
    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
