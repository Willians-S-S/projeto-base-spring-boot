package br.com.wss.filters;

import br.com.wss.projeto.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserContextDetails {
    private String username;
    private Account account;
    private String jwtToken;
}