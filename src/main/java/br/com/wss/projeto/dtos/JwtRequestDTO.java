package br.com.wss.projeto.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDTO {
    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;
    
    @Size(min=8, message = "Senha deve conter no mínimo 8 caracteres")
    private String password;
}
