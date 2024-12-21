package com.example.Demo.model.dto;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;

    public UserDTO(String email,String firstName,String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public UserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
