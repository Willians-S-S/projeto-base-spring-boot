package br.com.wss.projeto.repositories.projections;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.wss.projeto.enums.RoleAccountEnum;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface AccountProjection {

    String getUid();

    LocalDateTime getCreatedAt();

    String getCreatedByName();

    LocalDateTime getUpdatedAt();

    String getUpdatedByName();

    String getName();

    String getTaxNumber();

    RoleAccountEnum getRole();

    String getPhone();

    String getEmail();

}
