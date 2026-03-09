package br.com.wss.filters;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import br.com.wss.exception.BusinessException;
import br.com.wss.projeto.entities.Account;
import br.com.wss.projeto.repositories.AccountRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserContext {

    private final AccountRepository accountRepository;

    public UserContextDetails getUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
                || authentication.getPrincipal().equals("anonymousUser")) {
            return new UserContextDetails("anonymousUser", null, null);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String uid = jwt.getSubject();

        Account account = accountRepository.findById(uid)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        return new UserContextDetails(account.getName(), account, String.valueOf(jwt));
    }
}
