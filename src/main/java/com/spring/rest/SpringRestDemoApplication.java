package com.spring.rest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;

/**
 * @author Srinivas Komatipally
 */
@SpringBootApplication
public class SpringRestDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRestDemoApplication.class, args);
    }

}

@Configuration
@Slf4j
class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        repository.save(new User("Srini", "Komatipally"));
        return null;
    }
}

@Entity
@Data
@NoArgsConstructor
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

@RestController
class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    @ResponseBody
    public String welcome() {
        return "Hello World!";
    }

    @GetMapping("/users")
    @ResponseBody
    public Iterable<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Integer id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable(value = "id") Integer id) {
        user.setId(id);
        return userService.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") Integer id) {
        User user = userService.getUser(id);
        userService.delete(user);
    }
}

@Repository
interface UserRepository extends CrudRepository<User, Integer> {
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Integer id) {
        return userRepository.findById(id).orElse(null);

    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}