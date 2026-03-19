package br.com.wss.projeto.resources;

import br.com.wss.exception.BusinessException;
import br.com.wss.projeto.business.AccountBusiness;
import br.com.wss.projeto.business.VerificationCodeBusiness;
import br.com.wss.projeto.dtos.SendVerificationCodeDTO;
import br.com.wss.projeto.dtos.ValidateVerificationCodeDTO;
import br.com.wss.projeto.entities.Account;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VerificationCodeResource {

    private final VerificationCodeBusiness verificationCodeBusiness;
    private final AccountBusiness accountBusiness;

    public static final String VERIFICATION_CODES = "/verification-codes";
    public static final String SEND_CODE = VERIFICATION_CODES + "/send";
    public static final String VALIDATE_CODE = VERIFICATION_CODES + "/validate";

    @Operation(summary = "Enviar código de verificação")
    @PostMapping(SEND_CODE)
    public ResponseEntity<Void> sendCode(@RequestBody @Valid final SendVerificationCodeDTO dto) {
        Account account = accountBusiness.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada para o e-mail informado."));

        verificationCodeBusiness.sendVerificationCode(account, dto.getType());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validar código de verificação")
    @PostMapping(VALIDATE_CODE)
    public ResponseEntity<Void> validateCode(@RequestBody @Valid final ValidateVerificationCodeDTO dto) {
        Account account = accountBusiness.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada para o e-mail informado."));

        verificationCodeBusiness.validateCode(account, dto.getCode(), dto.getType());
        return ResponseEntity.ok().build();
    }
}
