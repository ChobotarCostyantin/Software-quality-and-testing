package edu.chobotar.lab5.service;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import edu.chobotar.lab5.request.UserCreateRequest;
import edu.chobotar.lab5.request.UserUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/*
  @author User
  @project Lab5
  @class UserServiceTest
  @version 1.0.0
  @since 22.05.2025 - 17.19
*/

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService underTest;

    @BeforeEach
    void setUp() {
        List<User> freshUsers = List.of(
                new User("Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "Мопс"),
                new User("2", "Валерій Адамко", "Roxaan", "adamko@gmail.com", "0957654321", "Бандеромобіль"),
                new User("Михайло Скорейко", "Tesey", "skoreyko@gmail.com", "0951237654", "Бомбардіро Крокоділо"),
                new User("4", "В'ячеслав Москалюк", "Ikaut", "moskaliuk@gmail.com", "0985358765", "Швайн")
        );
        repository.saveAll(freshUsers);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void whenGetAll_ThenReturnInitializedUsers() {
        // when
        List<User> users = underTest.getAll();

        // then
        assertNotNull(users);
        assertEquals(4, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getName().equals("Олег Максимчук")));
        assertTrue(users.stream().anyMatch(user -> user.getName().equals("Валерій Адамко")));
        assertTrue(users.stream().anyMatch(user -> user.getName().equals("Михайло Скорейко")));
        assertTrue(users.stream().anyMatch(user -> user.getName().equals("В'ячеслав Москалюк")));
    }

    @Test
    void whenGetById_WithValidId_ThenReturnUser() {
        // given
        List<User> allUsers = underTest.getAll();
        String validId = allUsers.get(0).getId();

        // when
        User foundUser = underTest.getById(validId);

        // then
        assertNotNull(foundUser);
        assertEquals(validId, foundUser.getId());
    }

    @Test
    void whenGetById_WithInvalidId_ThenThrowException() {
        // given
        String invalidId = "nonexistent-id";

        // when & then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> underTest.getById(invalidId));
        assertEquals("User with id " + invalidId + " not found", exception.getMessage());
    }

    @Test
    void whenCreateUser_WithValidData_ThenUserIsCreated() {
        // given
        User user = new User("Test User", "testuser", "test@example.com", "0501234567", "Male");

        // when
        User createdUser = underTest.create(user);

        // then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("0501234567", createdUser.getPhoneNumber());
        assertEquals("Male", createdUser.getGender());
    }

    @Test
    void whenCreateUserWithRequest_ThenCreateDateIsPresent() {
        // given
        UserCreateRequest request = new UserCreateRequest("John Doe", "johndoe", "john@example.com", "0509876543", "Male");
        LocalDateTime now = LocalDateTime.now();

        // when
        User createdUser = underTest.create(request);

        // then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
        assertEquals("johndoe", createdUser.getUsername());
        assertEquals("john@example.com", createdUser.getEmail());
        assertEquals("0509876543", createdUser.getPhoneNumber());
        assertEquals("Male", createdUser.getGender());
        assertNotNull(createdUser.getCreateDate());
        assertSame(LocalDateTime.class, createdUser.getCreateDate().getClass());
        assertTrue(createdUser.getCreateDate().isAfter(now));
        assertNotNull(createdUser.getUpdateDate());
        assertSame(ArrayList.class, createdUser.getUpdateDate().getClass());
        assertTrue(createdUser.getUpdateDate().isEmpty());
    }

    @Test
    void whenCreateUserWithRequest_WithDuplicateEmail_ThenThrowException() {
        // given
        UserCreateRequest firstRequest = new UserCreateRequest("User One", "user1", "duplicate@example.com", "0501111111", "Male");
        UserCreateRequest secondRequest = new UserCreateRequest("User Two", "user2", "duplicate@example.com", "0502222222", "Female");

        underTest.create(firstRequest);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> underTest.create(secondRequest));
        assertEquals("User with email duplicate@example.com already exists", exception.getMessage());
    }

    @Test
    void whenUpdateUser_WithValidData_ThenUserIsUpdated() {
        // given
        User originalUser = new User("Original Name", "original", "original@example.com", "0501111111", "Male");
        User savedUser = underTest.create(originalUser);

        savedUser.setName("Updated Name");
        savedUser.setUsername("updated");

        // when
        User updatedUser = underTest.update(savedUser);

        // then
        assertNotNull(updatedUser);
        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated", updatedUser.getUsername());
    }

    @Test
    void whenUpdateUserWithRequest_ThenUpdateDateIsAdded() {
        // given
        UserCreateRequest createRequest = new UserCreateRequest("Test User", "testuser", "test@example.com", "0501234567", "Male");
        User createdUser = underTest.create(createRequest);

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                createdUser.getId(),
                "Updated User",
                "updateduser",
                "updated@example.com",
                "0509876543",
                "Female"
        );
        LocalDateTime beforeUpdate = LocalDateTime.now();

        // when
        User updatedUser = underTest.update(updateRequest);

        // then
        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("0509876543", updatedUser.getPhoneNumber());
        assertEquals("Female", updatedUser.getGender());
        assertEquals(createdUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS), updatedUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS));
        assertNotNull(updatedUser.getUpdateDate());
        assertEquals(1, updatedUser.getUpdateDate().size());
        assertTrue(updatedUser.getUpdateDate().get(0).isAfter(beforeUpdate));
    }

    @Test
    void whenUpdateUserWithRequest_WithInvalidId_ThenReturnNull() {
        // given
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "invalid-id",
                "Updated User",
                "updateduser",
                "updated@example.com",
                "0509876543",
                "Female"
        );

        // when
        User result = underTest.update(updateRequest);

        // then
        assertNull(result);
    }

    @Test
    void whenUpdateUserMultipleTimes_ThenMultipleUpdateDatesAreAdded() {
        // given
        UserCreateRequest createRequest = new UserCreateRequest("Test User", "testuser", "test@example.com", "0501234567", "Male");
        User createdUser = underTest.create(createRequest);

        UserUpdateRequest firstUpdate = new UserUpdateRequest(
                createdUser.getId(), "First Update", "firstupdate", "first@example.com", "0501111111", "Male"
        );
        UserUpdateRequest secondUpdate = new UserUpdateRequest(
                createdUser.getId(), "Second Update", "secondupdate", "second@example.com", "0502222222", "Female"
        );

        // when
        User firstUpdated = underTest.update(firstUpdate);
        User secondUpdated = underTest.update(secondUpdate);

        // then
        assertNotNull(secondUpdated);
        assertEquals(2, secondUpdated.getUpdateDate().size());
        assertTrue(secondUpdated.getUpdateDate().get(1).isAfter(secondUpdated.getUpdateDate().get(0)));
    }

    @Test
    void whenDeleteById_WithValidId_ThenUserIsDeleted() {
        // given
        UserCreateRequest request = new UserCreateRequest("To Delete", "todelete", "delete@example.com", "0501234567", "Male");
        User createdUser = underTest.create(request);
        String userId = createdUser.getId();

        // when
        underTest.deleteById(userId);

        // then
        assertThrows(NoSuchElementException.class, () -> underTest.getById(userId));
    }

    @Test
    void whenDeleteById_WithInvalidId_ThenNoExceptionThrown() {
        // given
        String invalidId = "nonexistent-id";

        // when & then
        assertDoesNotThrow(() -> underTest.deleteById(invalidId));
    }

    @Test
    void whenCreateUserWithEmptyFields_ThenUserIsCreatedWithEmptyValues() {
        // given
        UserCreateRequest request = new UserCreateRequest("", "", "", "", "");

        // when
        User createdUser = underTest.create(request);

        // then
        assertNotNull(createdUser);
        assertEquals("", createdUser.getName());
        assertEquals("", createdUser.getUsername());
        assertEquals("", createdUser.getEmail());
        assertEquals("", createdUser.getPhoneNumber());
        assertEquals("", createdUser.getGender());
    }

    @Test
    void whenCreateUserWithNullFields_ThenUserIsCreatedWithNullValues() {
        // given
        UserCreateRequest request = new UserCreateRequest(null, null, null, null, null);

        // when
        User createdUser = underTest.create(request);

        // then
        assertNotNull(createdUser);
        assertNull(createdUser.getName());
        assertNull(createdUser.getUsername());
        assertNull(createdUser.getEmail());
        assertNull(createdUser.getPhoneNumber());
        assertNull(createdUser.getGender());
    }

    @Test
    void whenGetAllAfterCreatingNewUser_ThenReturnAllUsers() {
        // given
        int initialCount = underTest.getAll().size();
        UserCreateRequest request = new UserCreateRequest("New User", "newuser", "new@example.com", "0501234567", "Male");

        // when
        underTest.create(request);
        List<User> allUsers = underTest.getAll();

        // then
        assertEquals(initialCount + 1, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(user -> "New User".equals(user.getName())));
    }

    @Test
    void whenCreateUserWithSpecialCharacters_ThenUserIsCreated() {
        // given
        UserCreateRequest request = new UserCreateRequest("Тест Юзер", "тестюзер123", "тест@укр.нет", "+380501234567", "Чоловік");

        // when
        User createdUser = underTest.create(request);

        // then
        assertNotNull(createdUser);
        assertEquals("Тест Юзер", createdUser.getName());
        assertEquals("тестюзер123", createdUser.getUsername());
        assertEquals("тест@укр.нет", createdUser.getEmail());
        assertEquals("+380501234567", createdUser.getPhoneNumber());
        assertEquals("Чоловік", createdUser.getGender());
    }

    @Test
    void whenUpdateUserPreservesCreateDate_ThenCreateDateRemainsSame() {
        // given
        UserCreateRequest createRequest = new UserCreateRequest("Original", "original", "original@example.com", "0501234567", "Male");
        User createdUser = underTest.create(createRequest);
        LocalDateTime originalCreateDate = createdUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS);

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                createdUser.getId(), "Updated", "updated", "updated@example.com", "0509876543", "Female"
        );

        // when
        User updatedUser = underTest.update(updateRequest);

        // then
        assertNotNull(updatedUser);
        assertEquals(originalCreateDate, updatedUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void whenGetByIdWithExistingUser_ThenReturnCorrectUserData() {
        // given
        UserCreateRequest request = new UserCreateRequest("Specific User", "specificuser", "specific@example.com", "0501234567", "Non-binary");
        User createdUser = underTest.create(request);

        // when
        User foundUser = underTest.getById(createdUser.getId());

        // then
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("Specific User", foundUser.getName());
        assertEquals("specificuser", foundUser.getUsername());
        assertEquals("specific@example.com", foundUser.getEmail());
        assertEquals("0501234567", foundUser.getPhoneNumber());
        assertEquals("Non-binary", foundUser.getGender());
        assertEquals(createdUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS), foundUser.getCreateDate().truncatedTo(ChronoUnit.MILLIS));
        assertEquals(createdUser.getUpdateDate(), foundUser.getUpdateDate());
    }

    @Test
    void whenCheckInitializationData_ThenCorrectUsersArePresent() {
        // when
        List<User> users = underTest.getAll();

        // then
        assertNotNull(users);

        // Check specific users from initialization
        assertTrue(users.stream().anyMatch(user ->
                "Олег Максимчук".equals(user.getName()) &&
                        "Tom_oleg".equals(user.getUsername()) &&
                        "maksimchuk@gmail.com".equals(user.getEmail())
        ));

        assertTrue(users.stream().anyMatch(user ->
                "Валерій Адамко".equals(user.getName()) &&
                        "Roxaan".equals(user.getUsername()) &&
                        "adamko@gmail.com".equals(user.getEmail())
        ));

        assertTrue(users.stream().anyMatch(user ->
                "Михайло Скорейко".equals(user.getName()) &&
                        "Tesey".equals(user.getUsername()) &&
                        "skoreyko@gmail.com".equals(user.getEmail())
        ));

        assertTrue(users.stream().anyMatch(user ->
                "В'ячеслав Москалюк".equals(user.getName()) &&
                        "Ikaut".equals(user.getUsername()) &&
                        "moskaliuk@gmail.com".equals(user.getEmail())
        ));
    }

    @Test
    void whenCreateUserWithLongValues_ThenUserIsCreatedSuccessfully() {
        // given
        String longName = "Дуже Довге Ім'я Користувача Яке Може Містити Багато Символів";
        String longUsername = "verylongusernamethatcouldpotentiallyexceedtypicallengths123456789";
        String longEmail = "verylongemailaddressthatmightbeusedinsomespecialcases@verylongdomainname.com";
        String longPhone = "+380123456789012345";
        String longGender = "Складна гендерна ідентичність";

        UserCreateRequest request = new UserCreateRequest(longName, longUsername, longEmail, longPhone, longGender);

        // when
        User createdUser = underTest.create(request);

        // then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(longName, createdUser.getName());
        assertEquals(longUsername, createdUser.getUsername());
        assertEquals(longEmail, createdUser.getEmail());
        assertEquals(longPhone, createdUser.getPhoneNumber());
        assertEquals(longGender, createdUser.getGender());
        assertNotNull(createdUser.getCreateDate());
        assertNotNull(createdUser.getUpdateDate());
        assertTrue(createdUser.getUpdateDate().isEmpty());
    }
}