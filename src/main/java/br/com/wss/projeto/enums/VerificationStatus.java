package br.com.wss.projeto.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import br.com.wss.exception.BusinessException;
import lombok.Getter;

@Getter
public enum VerificationStatus {
    PENDING("Aguardando verificação"),
    VERIFIED("Código validado com sucesso"),
    EXPIRED("Tempo esgotado"),
    INVALID("Código inválido"),
    EXHAUSTED("Tentativas esgotadas");

    private final String name;

    VerificationStatus(final String name) {
        this.name = name;
    }

    public static VerificationStatus of(final String value) {

        for (VerificationStatus verificationStatus : Arrays.asList(VerificationStatus.values())) {
            if (verificationStatus.getName().equals(value))
                return verificationStatus;
        }

        throw new BusinessException(HttpStatus.BAD_REQUEST,
                "Os valores válidos são: " + Arrays.stream(VerificationStatus.values()).map(VerificationStatus::getName)
                        .toList().toString());
    }

    public static Stream<Map<String, String>> getValues() {
        return Arrays.stream(VerificationStatus.values()).map(en -> Map.of(en.name(), en.getName()));
    }
}
