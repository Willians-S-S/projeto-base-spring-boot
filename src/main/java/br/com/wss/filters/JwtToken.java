package br.com.wss.filters;

import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import br.com.wss.projeto.entities.Account;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class JwtToken {

    private final JwtEncoder encoder;

    private static final long JWT_TOKEN_VALIDITY = (60 * 60 * 24 * 2);

    public String genereteToken(Account account){
        try {
            Instant now = Instant.now();

            var scopes = account.getRoleAccountEnum() != null ? account.getRoleAccountEnum() : "USER";

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("wss")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(JWT_TOKEN_VALIDITY))
                    .subject(account.getUid())
                    .claim("scope", scopes)
                    .build();

            var encoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS256).build(),
                    claims
            );

            return encoder.encode(encoderParameters).getTokenValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar token: " + e.getMessage());
        }
    }

}
