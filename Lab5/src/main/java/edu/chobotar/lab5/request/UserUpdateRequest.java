package edu.chobotar.lab5.request;

/*
  @author User
  @project Lab5
  @class UserUpdateRequest
  @version 1.0.0
  @since 22.05.2025 - 16.41
*/
public record UserUpdateRequest(String id, String name, String username, String email, String phoneNumber, String gender) {
}
