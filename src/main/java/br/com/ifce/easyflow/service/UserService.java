package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.repository.UserRepository;
import br.com.ifce.easyflow.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user){
        return this.userRepository.save(user);
    }

    public List<User> search(){
        return this.userRepository.findAll();
    }

    public Optional<User> searchByID(Long id){
        return this.userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login){
        return this.userRepository.findByLogin(login);
    }

    @Transactional
    public Optional<User> update(User newUser){
        Optional<User> oldUser = this.searchByID(newUser.getId());

        return oldUser.isPresent()
                ? Optional.of(this.save(this.fillUpdateUser(oldUser.get(),newUser)))
                : Optional.empty();
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<User> user = this.searchByID(id);

        if(user.isPresent()){
            this.userRepository.delete(user.get());
            return true;
        }

        return false;
    }

    public Optional<User> searchByLogin(String login){
        return this.userRepository.findByLogin(login);
    }

    private User fillUpdateUser(User oldUser,User newUser){
        newUser.setPassword(oldUser.getPassword());
        newUser.setActive(oldUser.getActive());

        return newUser;
    }

    public boolean existsByLogin(String login) {
        Optional<User> exist = this.userRepository.findByLogin(login);

        return exist.isPresent();
    }


    public boolean existsByID(Long id) {
        Optional<User> exist = this.userRepository.findById(id);

        return exist.isPresent();
    }
}
