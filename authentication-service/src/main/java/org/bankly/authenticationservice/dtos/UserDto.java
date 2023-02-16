package org.bankly.authenticationservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String phoneNumber;
    @NotNull
    @Column(updatable = false,unique = true)
    private String email;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
