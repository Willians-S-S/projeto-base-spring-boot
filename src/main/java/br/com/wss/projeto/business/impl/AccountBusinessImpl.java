package br.com.wss.projeto.business.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.wss.base.AbstractBusinessImpl;
import br.com.wss.base.TransactionType;
import br.com.wss.exception.BusinessException;
import br.com.wss.filters.UserContextDetails;
import br.com.wss.projeto.business.AccountBusiness;
import br.com.wss.projeto.entities.Account;
import br.com.wss.projeto.enums.RoleAccountEnum;
import br.com.wss.projeto.repositories.AccountRepository;
import br.com.wss.services.EmailService;
import br.com.wss.services.model.Email;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
@AllArgsConstructor
public class AccountBusinessImpl extends AbstractBusinessImpl<Account, String> implements AccountBusiness {

    @Getter
    private final AccountRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public Optional<Account> findByEmail(String email){
        return getRepository().findByEmail(email);
    }

    @Override
    public Account insert(final Account entity){

        validate(entity, TransactionType.INSERT);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        UserContextDetails userContextDetails = super.getUserDetails();

        RoleAccountEnum existingRoleAccountEnum = Optional.ofNullable(userContextDetails.getAccount())
                .map(Account::getRoleAccountEnum)
                .orElse(null);

        Account account = null;

        if (existingRoleAccountEnum == null) {
            entity.setRoleAccountEnum(RoleAccountEnum.ROLE_CLIENT);
            account = super.insert(entity);
        }

        Email email = new Email();
        email.setTo(entity.getEmail());
        email.setSubject("Bem-vindo ao sistema!");
        email.setTemplate("welcome-email");
        email.setVariables(Map.of("name", entity.getName()));

        try {
            emailService.sendEmail(email);
        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail de boas-vindas para {}: {}", entity.getEmail(), e.getMessage());
        }

        return account;
    }

    public List<Account> findAll(){
        return getRepository().findAll();
    }

    public Page<Account> findByParams(final String uid, final String name, final String taxNumber,
            final String email, final String phone, final String createdByName, final String updatedByName,
                                      final RoleAccountEnum roleAccountEnum, final Boolean active, final LocalDateTime createdStartAt, final LocalDateTime createdEndAt,
                                      final Pageable pageable){
        return getRepository().findByParams(uid, name, taxNumber, email, phone, createdByName, updatedByName, roleAccountEnum, active, createdStartAt, createdEndAt, pageable);
    }

    @Override
    protected void validate(final Account entity, final TransactionType transactionType) {
        getRepository().findByEmail(entity.getEmail()).ifPresent(acc -> {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este e-mail.");
        });

        getRepository().findByTaxNumber(entity.getTaxNumber()).ifPresent(acc -> {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este CPF/CNPJ.");
        });

        getRepository().findByPhone(entity.getPhone()).ifPresent(acc -> {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este número.");
        });
    }

    public Account getCurrentAccount(){
        Account loggedUser = super.getUserDetails().getAccount();

        return Optional.ofNullable(loggedUser).orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado."));
    }

    @Override
    public Account update(final Account entity) {
        Account loggedAccount = super.getUserDetails().getAccount();

        loggedAccount.setName(entity.getName());

        if(!loggedAccount.getEmail().equals(entity.getEmail())){
            getRepository().findByEmail(entity.getEmail())
                    .ifPresent(account -> {
                        throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este e-mail.");
                    });
            loggedAccount.setEmail(entity.getEmail());
        }

        if(!loggedAccount.getPhone().equals(entity.getPhone())){
            getRepository().findByPhone(entity.getPhone())
                    .ifPresent(account -> {
                        throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este número.");
                    });
            loggedAccount.setPhone(entity.getPhone());
        }

        if(!loggedAccount.getTaxNumber().equals(entity.getTaxNumber())){
            getRepository().findByTaxNumber(entity.getTaxNumber())
                    .ifPresent(account -> {
                        throw new BusinessException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com este número.");
                    });
            loggedAccount.setTaxNumber(entity.getTaxNumber());
        }

        loggedAccount.setUpdatedByName(loggedAccount.getName());
        loggedAccount.setUpdatedByUid(loggedAccount.getUid());

        return getRepository().save(loggedAccount);
    }

    @Override
    public void delete(){
        UserContextDetails userContextDetails = getUserDetails();
        super.delete(userContextDetails.getAccount().getUid());
    }

    @Override
    public void permanentlyDelete() {
        getRepository().permanentlyDelete();
    }

    @Override
    protected String getEntityId(Account entity) {
        return entity.getUid();
    }
}
