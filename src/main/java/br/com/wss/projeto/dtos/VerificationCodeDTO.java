package br.com.wss.projeto.dtos;

import java.time.LocalDateTime;

import br.com.wss.base.BaseDTO;
import br.com.wss.projeto.enums.VerificationStatus;
import br.com.wss.projeto.enums.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class VerificationCodeDTO extends BaseDTO<String> {

    private AccountDTO account;
    private String code;
    private VerificationType type;
    private VerificationStatus status;
    private LocalDateTime expiresAt;
    private Boolean revoked;
    private int attempts;
    private int maxAttempts;
    private LocalDateTime verifiedAt;

}
