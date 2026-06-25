package br.com.wss.projeto.dtos;

import br.com.wss.projeto.validation.PasswordValid;
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
    
    @PasswordValid
    private String password;
}
