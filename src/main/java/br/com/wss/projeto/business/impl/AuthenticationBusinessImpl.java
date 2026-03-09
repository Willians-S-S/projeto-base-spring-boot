package br.com.wss.projeto.business.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.wss.filters.JwtToken;
import br.com.wss.projeto.business.AccountBusiness;
import br.com.wss.projeto.business.AuthenticationBusiness;
import br.com.wss.projeto.dtos.JwtRequestDTO;
import br.com.wss.projeto.dtos.JwtResponseDTO;
import br.com.wss.projeto.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Component
@Slf4j
public class AuthenticationBusinessImpl implements AuthenticationBusiness {


    private AccountBusiness accountBusiness;

    private PasswordEncoder passwordEncoder;

    private JwtToken jwtToken;

    private static final String INVALID_CREDENTIALS = "Credenciais inválidas";

    @Override
    public JwtResponseDTO authentication(JwtRequestDTO authenticationRequest) {

        Account account = accountBusiness.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), account.getPassword()))
            throw new BadCredentialsException(INVALID_CREDENTIALS);

        return new JwtResponseDTO(jwtToken.genereteToken(account));
    }
}
