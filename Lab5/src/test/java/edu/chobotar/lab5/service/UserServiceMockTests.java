package edu.chobotar.lab5.service;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import edu.chobotar.lab5.request.UserCreateRequest;
import edu.chobotar.lab5.request.UserUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/*
  @author User
  @project Lab5
  @class UserServiceMockTest
  @version 1.0.0
  @since 22.05.2025 - 18.39
*/
@SpringBootTest
class UserServiceMockTests {
    @Mock
    private UserRepository mockRepository;

    private UserService underTest;

    @Captor
    private ArgumentCaptor<User> argumentCaptor;

    private UserCreateRequest createRequest;
    private UserUpdateRequest updateRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserService(mockRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Create new User. Happy Path")
    @Test
    void whenCreateNewUserAndEmailNotExistsThenOk() {
        // given
        createRequest = new UserCreateRequest("Іван Іваненко", "ivan123", "ivan@gmail.com", "0951234567", "Чоловік");
        given(mockRepository.existsByEmail(createRequest.email())).willReturn(false);

        // when
        underTest.create(createRequest);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User userToSave = argumentCaptor.getValue();
        assertThat(userToSave.getName()).isEqualTo(createRequest.name());
        assertThat(userToSave.getUsername()).isEqualTo(createRequest.username());
        assertThat(userToSave.getEmail()).isEqualTo(createRequest.email());
        assertThat(userToSave.getPhoneNumber()).isEqualTo(createRequest.phoneNumber());
        assertThat(userToSave.getGender()).isEqualTo(createRequest.gender());
        assertNotNull(userToSave.getCreateDate());
        assertTrue(userToSave.getCreateDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(userToSave.getUpdateDate().isEmpty());
        verify(mockRepository).save(userToSave);
        verify(mockRepository, times(1)).existsByEmail(createRequest.email());
        verify(mockRepository, times(1)).save(userToSave);
    }

    @DisplayName("Create new User. Email already exists")
    @Test
    void whenCreateNewUserAndEmailExistsThenThrowException() {
        // given
        createRequest = new UserCreateRequest("Іван Іваненко", "ivan123", "ivan@gmail.com", "0951234567", "Чоловік");
        given(mockRepository.existsByEmail(createRequest.email())).willReturn(true);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            underTest.create(createRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("User with email " + createRequest.email() + " already exists");
        verify(mockRepository, times(1)).existsByEmail(createRequest.email());
        verify(mockRepository, never()).save(any(User.class));
    }

    @DisplayName("Get all users")
    @Test
    void whenGetAllUsersThenReturnList() {
        // given
        List<User> expectedUsers = Arrays.asList(
                new User("Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "Мопс"),
                new User("Валерій Адамко", "Roxaan", "adamko@gmail.com", "0957654321", "Бандеромобіль")
        );
        given(mockRepository.findAll()).willReturn(expectedUsers);

        // when
        List<User> actualUsers = underTest.getAll();

        // then
        assertThat(actualUsers).isEqualTo(expectedUsers);
        assertThat(actualUsers).hasSize(2);
        verify(mockRepository, times(1)).findAll();
    }

    @DisplayName("Get user by ID. Happy Path")
    @Test
    void whenGetUserByIdAndUserExistsThenReturnUser() {
        // given
        String userId = "1";
        user = new User("Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "Мопс");
        user.setId(userId);
        given(mockRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        User actualUser = underTest.getById(userId);

        // then
        assertThat(actualUser).isEqualTo(user);
        assertThat(actualUser.getId()).isEqualTo(userId);
        verify(mockRepository, times(1)).findById(userId);
    }

    @DisplayName("Get user by ID. User not found")
    @Test
    void whenGetUserByIdAndUserNotExistsThenThrowException() {
        // given
        String userId = "999";
        given(mockRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            underTest.getById(userId);
        });

        assertThat(exception.getMessage()).isEqualTo("User with id " + userId + " not found");
        verify(mockRepository, times(1)).findById(userId);
    }

    @DisplayName("Update user. Happy Path")
    @Test
    void whenUpdateUserAndUserExistsThenUpdateSuccessfully() {
        // given
        String userId = "1";
        LocalDateTime createDate = LocalDateTime.now().minusDays(1);
        List<LocalDateTime> previousUpdateDates = new ArrayList<>();

        User existingUser = User.builder()
                .id(userId)
                .name("Старе ім'я")
                .username("old_username")
                .email("old@gmail.com")
                .phoneNumber("0951111111")
                .gender("Чоловік")
                .createDate(createDate)
                .updateDate(previousUpdateDates)
                .build();

        updateRequest = new UserUpdateRequest(userId, "Нове ім'я", "new_username",
                "new@gmail.com", "0952222222", "Жінка");

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        underTest.update(updateRequest);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User updatedUser = argumentCaptor.getValue();
        assertThat(updatedUser.getId()).isEqualTo(userId);
        assertThat(updatedUser.getName()).isEqualTo(updateRequest.name());
        assertThat(updatedUser.getUsername()).isEqualTo(updateRequest.username());
        assertThat(updatedUser.getEmail()).isEqualTo(updateRequest.email());
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(updateRequest.phoneNumber());
        assertThat(updatedUser.getGender()).isEqualTo(updateRequest.gender());
        assertThat(updatedUser.getCreateDate()).isEqualTo(createDate);
        assertThat(updatedUser.getUpdateDate()).hasSize(1);
        assertTrue(updatedUser.getUpdateDate().get(0).isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, times(1)).save(updatedUser);
    }

    @DisplayName("Update user. User not found")
    @Test
    void whenUpdateUserAndUserNotExistsThenReturnNull() {
        // given
        String userId = "999";
        updateRequest = new UserUpdateRequest(userId, "Нове ім'я", "new_username",
                "new@gmail.com", "0952222222", "Жінка");

        given(mockRepository.findById(userId)).willReturn(Optional.empty());

        // when
        User result = underTest.update(updateRequest);

        // then
        assertNull(result);
        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, never()).save(any(User.class));
    }

    @DisplayName("Delete user by ID")
    @Test
    void whenDeleteUserByIdThenCallRepositoryDelete() {
        // given
        String userId = "1";

        // when
        underTest.deleteById(userId);

        // then
        verify(mockRepository, times(1)).deleteById(userId);
    }

    @DisplayName("Create user using User object")
    @Test
    void whenCreateUserWithUserObjectThenSave() {
        // given
        user = new User("Петро Петренко", "petro123", "petro@gmail.com", "0951234567", "Чоловік");
        given(mockRepository.save(user)).willReturn(user);

        // when
        User result = underTest.create(user);

        // then
        assertThat(result).isEqualTo(user);
        verify(mockRepository, times(1)).save(user);
    }

    @DisplayName("Update user with existing update dates")
    @Test
    void whenUpdateUserWithExistingUpdateDatesThenAddNewDate() {
        // given
        String userId = "1";
        LocalDateTime createDate = LocalDateTime.now().minusDays(2);
        LocalDateTime firstUpdateDate = LocalDateTime.now().minusDays(1);
        List<LocalDateTime> existingUpdateDates = new ArrayList<>();
        existingUpdateDates.add(firstUpdateDate);

        User existingUser = User.builder()
                .id(userId)
                .name("Старе ім'я")
                .username("old_username")
                .email("old@gmail.com")
                .phoneNumber("0951111111")
                .gender("Чоловік")
                .createDate(createDate)
                .updateDate(existingUpdateDates)
                .build();

        updateRequest = new UserUpdateRequest(userId, "Оновлене ім'я", "updated_username",
                "updated@gmail.com", "0953333333", "Жінка");

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        underTest.update(updateRequest);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User updatedUser = argumentCaptor.getValue();
        assertThat(updatedUser.getUpdateDate()).hasSize(2);
        assertThat(updatedUser.getUpdateDate().get(0)).isEqualTo(firstUpdateDate);
        assertTrue(updatedUser.getUpdateDate().get(1).isAfter(firstUpdateDate));
        assertTrue(updatedUser.getUpdateDate().get(1).isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, times(1)).save(updatedUser);
    }
}