package com.Signing.JWTImplementation.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    @NotBlank

    private String firstName;
    @NotBlank

    private String lastName;
    @NotBlank

    private String email;
    @NotBlank

    @Length(min = 6, max = 8)
    private  String  password;
    @NotBlank

    private char gender;
    private Collection<String> role=new ArrayList<>();

}
