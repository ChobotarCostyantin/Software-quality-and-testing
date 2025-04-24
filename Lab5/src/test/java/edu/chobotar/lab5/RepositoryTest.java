package edu.chobotar.lab5;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/*
  @author User
  @project Lab5
  @class RepositoryTest
  @version 1.0.0
  @since 24.04.2025 - 23.26
*/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class RepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeAll
    void beforeAll() {}

    @BeforeEach
    void setUp() {
        List<User> freshUsers = List.of(
                new User("1", "Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "###test"),
                new User("2", "Валерій Адамко", "Roxaan", "adamko@gmail.com", "0957654321", "###test"),
                new User("3", "Михайло Скорейко", "Tesey", "skoreyko@gmail.com", "0951237654", "###test"),
                new User("4", "В'ячеслав Москалюк", "Ikaut", "moskaliuk@gmail.com", "0985358765", "###test")
        );
        underTest.saveAll(freshUsers);
    }

    @AfterEach
    void tearDown() {
        List<User> usersToDelete = underTest.findAll().stream()
                .filter(user -> user.getGender().contains("###test"))
                .toList();
        underTest.deleteAll(usersToDelete);
    }

    @AfterAll
    void afterAll() {}

    @Test
    void shouldFindAllUsers() {
        // given
        List<User> users = new ArrayList<>(underTest.findAll());
        // when
        List<User> foundUsers = underTest.findAll();

        // then
        assertEquals(4, foundUsers.size());
        assertTrue(foundUsers.containsAll(users));
    }

    @Test
    void shouldNotFindUserWithInvalidId() {
        // given
        String id = "999";

        // when
        Optional<User> foundUser = underTest.findById(id);

        // then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldFindUserById() {
        // given
        String id = "1";

        // when
        Optional<User> foundUser = underTest.findById(id);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("Олег Максимчук", foundUser.get().getName());
    }

    @Test
    void shouldSaveNewUser() {
        // given
        User newUser = new User("5", "Іван Петренко", "iPetrenko", "petrenko@gmail.com", "0671234567", "Козак###test");

        // when
        User savedUser = underTest.save(newUser);

        // then
        assertEquals(newUser.getId(), savedUser.getId());
        assertEquals(5, underTest.findAll().size());

        Optional<User> foundUser = underTest.findById("5");
        assertTrue(foundUser.isPresent());
        assertEquals("Іван Петренко", foundUser.get().getName());
    }

    @Test
    void shouldUpdateExistingUser() {
        // given
        List<User> users = new ArrayList<>(underTest.findAll());
        User userToUpdate = users.get(0);
        userToUpdate.setName("Олег Максимчук Оновлений");

        // when
        User updatedUser = underTest.save(userToUpdate);

        // then
        assertEquals("Олег Максимчук Оновлений", updatedUser.getName());

        Optional<User> foundUser = underTest.findById("1");
        assertTrue(foundUser.isPresent());
        assertEquals("Олег Максимчук Оновлений", foundUser.get().getName());
    }

    @Test
    void shouldDeleteUserById() {
        // given
        String id = "2";

        // when
        underTest.deleteById(id);

        // then
        assertEquals(3, underTest.findAll().size());
        assertTrue(underTest.findById(id).isEmpty());
    }

    @Test
    void shouldDeleteAllUsers() {
        // when
        underTest.deleteAll();

        // then
        assertEquals(0, underTest.findAll().size());
    }

    @Test
    void shouldFindUserByUsername() {
        // given
        String username = "Tesey";

        // when
        Optional<User> foundUser = underTest.findByUsername(username);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("Михайло Скорейко", foundUser.get().getName());
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        String email = "moskaliuk@gmail.com";

        // when
        Optional<User> foundUser = underTest.findByEmail(email);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("В'ячеслав Москалюк", foundUser.get().getName());
    }

    @Test
    void shouldFindUsersByPhoneNumberStartingWith() {
        // given
        String phonePrefix = "095";

        // when
        List<User> foundUsers = underTest.findByPhoneNumberStartingWith(phonePrefix);

        // then
        assertEquals(3, foundUsers.size());
        for (User user : foundUsers) {
            assertTrue(user.getPhoneNumber().startsWith(phonePrefix));
        }
    }
}