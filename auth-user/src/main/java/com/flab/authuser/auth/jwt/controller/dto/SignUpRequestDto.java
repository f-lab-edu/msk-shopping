package com.flab.authuser.auth.jwt.controller.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class SignUpRequestDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @NotEmpty
    private Boolean isAdmin;


    //==Constructor==//
    public SignUpRequestDto() {
    }

    public SignUpRequestDto(String email, String password, Boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}