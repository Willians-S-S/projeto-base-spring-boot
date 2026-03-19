package br.com.wss.projeto.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import br.com.wss.exception.BusinessException;
import lombok.Getter;

@Getter
public enum VerificationType {
    EMAIL_VERIFICATION("Verificação de Email"), 
    PASSWORD_RECOVERY("Recuperação de Senha"),
    ACCOUNT_RECOVERY("Recuperação de Conta"); 
    
    private final String name;

    VerificationType(final String name) {
        this.name = name;
    }

    public static VerificationType of(final String value) {

        for (VerificationType verificationType : Arrays.asList(VerificationType.values())) {
            if (verificationType.getName().equals(value))
                return verificationType;
        }

        throw new BusinessException(HttpStatus.BAD_REQUEST,
                "Os valores válidos são: " + Arrays.stream(VerificationType.values()).map(VerificationType::getName)
                        .toList().toString());
    }

    public static Stream<Map<String, String>> getValues() {
        return Arrays.stream(VerificationType.values()).map(en -> Map.of(en.name(), en.getName()));
    }
}
