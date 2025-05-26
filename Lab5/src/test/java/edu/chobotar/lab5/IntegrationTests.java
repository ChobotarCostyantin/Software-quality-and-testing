package edu.chobotar.lab5;

/*
  @author User
  @project Lab5
  @class IntegrationTests
  @version 1.0.0
  @since 22.05.2025 - 21.05
*/

import edu.chobotar.lab5.Utils.Utils;
import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import edu.chobotar.lab5.request.UserCreateRequest;
import edu.chobotar.lab5.request.UserUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
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

    @DisplayName("Create new user. Happy path")
    @Test
    void itShouldCreateNewUser() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "Новий Користувач", "NewUser", "user@gmail.com", "0957654321", "NaN");

        // when
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)));

        // then
        User user = repository.findByEmail(request.email()).orElse(null);
        perform.andExpect(status().isOk());
        assertTrue(repository.existsByEmail(request.email()));
        assertNotNull(user);
        assertNotNull(user.getId());
        assertThat(user.getId()).isNotEmpty();
        assertThat(user.getId().length()).isEqualTo(24);
        assertThat(user.getName()).isEqualTo(request.name());
        assertThat(user.getUsername()).isEqualTo(request.username());
        assertThat(user.getEmail()).isEqualTo(request.email());
        assertThat(user.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(user.getGender()).isEqualTo(request.gender());
        assertThat(user.getUpdateDate()).isEmpty();
        assertThat(user.getCreateDate()).isNotNull();
        assertSame(LocalDateTime.class, user.getCreateDate().getClass());
    }

    @DisplayName("Create new user. Email is used")
    @Test
    void itShouldNotCreateUserWhenEmailIsUsed() throws Exception {
        // given
        UserCreateRequest duplicateRequest = new UserCreateRequest(
                "Новий Користувач", "NewUser", "adamko@gmail.com", "0957654321", "NaN");

        // when
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(duplicateRequest)));

        // then
        perform.andExpect(status().isBadRequest());

        List<User> users = repository.findAll();
        User userWithSameEmail = users.stream()
                .filter(user -> user.getEmail().equals(duplicateRequest.email()))
                .findFirst()
                .orElse(null);
        int userIndex = users.indexOf(userWithSameEmail);
        assertThat(users.size()).isEqualTo(4);
        assertThat(users.get(userIndex).getEmail()).isEqualTo("adamko@gmail.com");
    }

    @DisplayName("Update user. Happy path")
    @Test
    void itShouldUpdateExistingUser() throws Exception {
        // given
        UserCreateRequest user = new UserCreateRequest(
                "Новий Користувач", "NewUser", "user@gmail.com", "0957654321", "NaN");
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(user)));
        User usertoUpdate = repository.findByEmail(user.email()).orElse(null);
        assert usertoUpdate != null;
        String id = usertoUpdate.getId();
        UserUpdateRequest request = new UserUpdateRequest(
                id, "Новий Олег Максимчук", "Tom_oleg", "user@gmail.com", "0951234567", "Мопс");

        // when
        perform = mockMvc.perform(put("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)));

        // then
        User updatedUser = repository.findById(id).orElse(null);
        perform.andExpect(status().isOk());
        assertNotNull(updatedUser);
        assertThat(updatedUser.getName()).isEqualTo(request.name());
        assertThat(updatedUser.getUsername()).isEqualTo(request.username());
        assertThat(updatedUser.getEmail()).isEqualTo(request.email());
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(updatedUser.getGender()).isEqualTo(request.gender());
        assertThat(updatedUser.getUpdateDate()).isNotNull();
        assertThat(updatedUser.getCreateDate()).isNotNull();
        assertSame(LocalDateTime.class, updatedUser.getCreateDate().getClass());
        for (LocalDateTime updateDate : updatedUser.getUpdateDate()) {
            assertSame(LocalDateTime.class, updateDate.getClass());
        }
    }

    @DisplayName("Update user. Email is used")
    @Test
    void itShouldNotUpdateUserWhenEmailIsUsed() throws Exception {
        // given
        UserCreateRequest newUser = new UserCreateRequest(
                "Новий Користувач", "NewUser", "user@gmail.com", "0957654321", "NaN");
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(newUser)));
        User usertoUpdate = repository.findByEmail(newUser.email()).orElse(null);
        assert usertoUpdate != null;
        String id = usertoUpdate.getId();
        UserUpdateRequest request = new UserUpdateRequest(
                id, "Новий Користувач", "NewUser", "adamko@gmail.com", "0957654321", "NaN");

        // when
        perform = mockMvc.perform(put("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)));

        // then
        User updatedUser = repository.findById(id).orElse(null);
        perform.andExpect(status().isBadRequest());
        assertNotNull(updatedUser);
        assertThat(updatedUser.getName()).isEqualTo(request.name());
        assertThat(updatedUser.getUsername()).isEqualTo(request.username());
        assertThat(updatedUser.getEmail()).isNotEqualTo(request.email());
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(updatedUser.getGender()).isEqualTo(request.gender());
        assertThat(updatedUser.getUpdateDate()).isNotNull();
        assertThat(updatedUser.getCreateDate()).isNotNull();
        assertSame(LocalDateTime.class, updatedUser.getCreateDate().getClass());
        for (LocalDateTime updateDate : updatedUser.getUpdateDate()) {
            assertSame(LocalDateTime.class, updateDate.getClass());
        }
    }

    @DisplayName("Delete user. Happy path")
    @Test
    void itShouldDeleteExistingUser() throws Exception {
        // given
        User userToDelete = repository.findByEmail("adamko@gmail.com").orElse(null);
        assert userToDelete != null;
        String id = userToDelete.getId();

        // when
        ResultActions perform = mockMvc.perform(delete("http://localhost:8080/api/v1/users/{id}", id));

        // then
        perform.andExpect(status().isOk());
        assertFalse(repository.existsById(id));
    }

    @DisplayName("Delete user. User not found")
    @Test
    void itShouldNotDeleteUserWhenUserNotFound() throws Exception {
        // given
        String id = "999";

        // when
        ResultActions perform = mockMvc.perform(delete("http://localhost:8080/api/v1/users/{id}", id));

        // then
        perform.andExpect(status().isNotFound());
        assertFalse(repository.existsById(id));
    }

    @DisplayName("Get all users. Happy path")
    @Test
    void itShouldGetAllUsers() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("http://localhost:8080/api/v1/users"));

        // then
        perform.andExpect(status().isOk());
        assertThat(repository.count()).isEqualTo(4);
    }

    @DisplayName("Get all users. No users")
    @Test
    void itShouldGetAllUsersWhenNoUsers() throws Exception {
        // given
        repository.deleteAll();

        // when
        ResultActions perform = mockMvc.perform(get("http://localhost:8080/api/v1/users"));

        // then
        perform.andExpect(status().isOk());
        assertThat(repository.count()).isEqualTo(0);
    }

    @DisplayName("Get user by id. Happy path")
    @Test
    void itShouldGetUserById() throws Exception {
        // given
        repository.deleteAll();
        UserCreateRequest user = new UserCreateRequest(
                "Валерій Адамко", "adamko", "adamko@gmail.com", "0951234567", "Чоловік");
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/users/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(user)));
        User createdUser = repository.findByEmail(user.email()).orElse(null);
        assert createdUser != null;
        String id = createdUser.getId();

        // when
        perform = mockMvc.perform(get("http://localhost:8080/api/v1/users/{id}", id));

        // then
        perform.andExpect(status().isOk());
        assertNotNull(user);
        assertThat(createdUser.getName()).isEqualTo("Валерій Адамко");
        assertThat(createdUser.getUsername()).isEqualTo("adamko");
        assertThat(createdUser.getEmail()).isEqualTo("adamko@gmail.com");
        assertThat(createdUser.getPhoneNumber()).isEqualTo("0951234567");
        assertThat(createdUser.getGender()).isEqualTo("Чоловік");
        assertThat(createdUser.getUpdateDate()).isNotNull();
        assertThat(createdUser.getCreateDate()).isNotNull();
        assertSame(LocalDateTime.class, createdUser.getCreateDate().getClass());
        for (LocalDateTime updateDate : createdUser.getUpdateDate()) {
            assertSame(LocalDateTime.class, updateDate.getClass());
        }
    }

    @DisplayName("Get user by id. User not found")
    @Test
    void itShouldNotGetUserByIdWhenUserNotFound() throws Exception {
        // given
        String id = "999";

        // when
        ResultActions perform = mockMvc.perform(get("http://localhost:8080/api/v1/users/{id}", id));

        // then
        perform.andExpect(status().isNotFound());
        assertNull(repository.findById(id).orElse(null));
    }
}
