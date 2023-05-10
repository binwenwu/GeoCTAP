package com.gis.geoctap.controller.dto;

import lombok.Data;

/**
 * UserPasswordDTO
 */
@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newPassword;
}
