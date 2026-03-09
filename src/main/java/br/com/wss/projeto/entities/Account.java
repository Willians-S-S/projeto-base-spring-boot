package br.com.wss.projeto.entities;

import org.hibernate.annotations.DynamicUpdate;

import br.com.wss.base.BaseEntity;
import br.com.wss.projeto.enums.RoleAccountEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
//@Table(schema = GenericUtils.SCHEMA)
@Table(name = "ACCOUNT")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
public class Account extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String name;

    @Column(unique = true, nullable = false)
    private String taxNumber;

    @Enumerated(EnumType.STRING)
    private RoleAccountEnum roleAccountEnum;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
}
