package br.com.wss.projeto.entities;

import java.time.LocalDateTime;

import br.com.wss.base.BaseEntity;
import br.com.wss.projeto.enums.VerificationStatus;
import br.com.wss.projeto.enums.VerificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "verification_code", indexes = {
    @Index(columnList = "account_id, type, status"),
    @Index(columnList = "expires_at"),
    @Index(columnList = "code")
})
public class VerificationCode extends BaseEntity<String>{
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Column(nullable = false, length = 64)
    private String code;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean revoked = false;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private int maxAttempts = 3;
    
    private LocalDateTime verifiedAt;
    
    @Version
    private Long version;

}