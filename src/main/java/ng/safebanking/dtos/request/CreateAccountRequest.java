package ng.safebanking.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank(message = "Account name is required")
        @Size(min = 2, max = 100,
                message = "Account name must be between 2 and 100 characters")
        String accountName,
        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100,
                message = "Password must be between 8 and 100 characters")
        String password
) {

}
