package br.com.wss.projeto.business.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.wss.base.AbstractBusinessImpl;
import br.com.wss.exception.BusinessException;
import br.com.wss.projeto.business.VerificationCodeBusiness;
import br.com.wss.projeto.entities.Account;
import br.com.wss.projeto.entities.VerificationCode;
import br.com.wss.projeto.enums.VerificationStatus;
import br.com.wss.projeto.enums.VerificationType;
import br.com.wss.projeto.repositories.VerificationCodeRepository;
import br.com.wss.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
@AllArgsConstructor
public class VerificationCodeBusinessImpl extends AbstractBusinessImpl<VerificationCode, String>
        implements VerificationCodeBusiness {

    @Getter
    private final VerificationCodeRepository repository;

    private final EmailService emailService;

    private final SecureRandom secureRandom;

    private final PasswordEncoder passwordEncoder;


    // TODO: dentro de 3 dias se o usuário não validar o código, excluir a conta



    private static final int CODE_EXPIRY_MINUTES = 15;
    private static final int MAX_ATTEMPTS = 3;


    public void sendVerificationCode(Account account, VerificationType type) {
        
        getRepository().invalidatePreviousCodes(account, type);

        String rawCode = generateCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setAccount(account);
        verificationCode.setCode(hashCode(rawCode));
        verificationCode.setType(type);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES));
        verificationCode.setMaxAttempts(MAX_ATTEMPTS);

        insert(verificationCode);

        switch (type) {
            case VerificationType.EMAIL_VERIFICATION -> emailService.sendEmail(null);
            case VerificationType.PASSWORD_RECOVERY -> emailService.sendEmail(null);
            default -> System.out.println("");
        }

    }

    private String generateCode() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    @Transactional
    public void validateCode(Account account, String inputCode, VerificationType type) {
        VerificationCode code = getRepository()
            .findLatestActiveCode(account.getUid(), type)
            .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Nenhum código ativo encontrado."));

        if (isExpired(code)) {
            code.setStatus(VerificationStatus.EXPIRED);
            merge(code);
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Código expirado. Solicite um novo.");
        }

        if (code.getAttempts() >= code.getMaxAttempts()) {
            code.setStatus(VerificationStatus.EXHAUSTED);
            merge(code);
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Limite de tentativas atingido. Solicite um novo código.");
        }

        incrementAttempts(code);

        if (!isValid(inputCode, code)) {
            code.setStatus(VerificationStatus.INVALID);
            merge(code);
            int remaining = code.getMaxAttempts() - code.getAttempts();
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Código inválido. Tentativas restantes: " + remaining);
        }

        code.setStatus(VerificationStatus.VERIFIED);
        code.setRevoked(true);
        getRepository().save(code);
    }

    private String hashCode(String plainCode) {
        return passwordEncoder.encode(plainCode);
    }

    private boolean verifyCodeHash(String plainCode, String hash) {
        return passwordEncoder.matches(plainCode, hash);
    }

    private boolean isExpired(VerificationCode code) {
        return LocalDateTime.now().isAfter(code.getExpiresAt());
    }    

    private boolean isValid(final String plainCode, final VerificationCode code) {
        return !code.getRevoked() 
                && !isExpired(code) 
                && code.getAttempts() < code.getMaxAttempts()
                && verifyCodeHash(plainCode, code.getCode());
    }

    private void incrementAttempts(VerificationCode code) {
        code.setAttempts(code.getAttempts() + 1);
    }

    @Override
    public String getEntityId(VerificationCode entity) {
        return entity.getUid();
    }

}
