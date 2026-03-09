package br.com.wss.projeto.business;

import br.com.wss.projeto.dtos.JwtRequestDTO;
import br.com.wss.projeto.dtos.JwtResponseDTO;

public interface AuthenticationBusiness {

    public JwtResponseDTO authentication(final JwtRequestDTO authenticationRequest);
}
