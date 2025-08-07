package org.supersaasai.requests;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponse {
    public String message;
    public String token;

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }
}
