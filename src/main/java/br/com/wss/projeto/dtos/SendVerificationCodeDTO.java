package br.com.wss.projeto.dtos;

import br.com.wss.projeto.enums.VerificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendVerificationCodeDTO {

    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    @NotNull(message = "O tipo de verificação é obrigatório")
    private VerificationType type;
}
