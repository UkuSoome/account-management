package com.lhv.account.dto;

import com.lhv.account.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateAccountRequest {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 50, message = "Phone number cannot exceed 50 characters")
    @ValidPhoneNumber
    private String phoneNr;
}
