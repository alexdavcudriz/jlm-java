package wolox.training.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.authentication.IAuthentication;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UserPreconditionFailedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IAuthentication authentication;
    private final UserRepository userRepository;

    public UserController(IAuthentication authentication, UserRepository userRepository) {

        this.authentication = authentication;
        this.userRepository = userRepository;

    }

    /**
     * Method that gets authenticated user
     *
     * @return username (String)
     */
    @GetMapping("/principal")
    public String authenticatedCurrentUser() {

        return authentication.getAuthentication().getName();

    }

    /**
     * Method that gets all users
     *
     * @return all {@link User}
     */
    @GetMapping
    public Iterable<User> findAll() {

        return userRepository.findAll();

    }

    /**
     * Method that obtain a user by id
     *
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {

        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

    }

    /**
     * User saving method
     *
     * @param user: Request body ({@link User})
     * @return {@link User}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody User user) {

        return userRepository.save(user);

    }

    /**
     * User updating method
     *
     * @param user: Request body ({@link User})
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {

        if (user.getId() != id) {
            throw new UserPreconditionFailedException();
        }

        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return userRepository.save(user);

    }

    /**
     * User deleting method
     *
     * @param id: User identifier (Long)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);

    }

    /**
     * Method that adds a book
     *
     * @param book: Book to add ({@link Book})
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @PatchMapping("/{id}/add-book")
    public User addBook(@RequestBody Book book, @PathVariable Long id){

        User userFound = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        userFound.addBook(book);

        return userRepository.save(userFound);

    }

    /**
     * Method that removes a book
     *
     * @param book: Book to add ({@link Book})
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @PatchMapping("/{id}/remove-book")
    public User removeBook(@RequestBody Book book, @PathVariable Long id){

        User userFound = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        userFound.removeBook(book);

        return userRepository.save(userFound);

    }

}
