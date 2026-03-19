package br.com.wss.projeto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.wss.base.BaseRepository;
import br.com.wss.projeto.entities.Account;
import br.com.wss.projeto.entities.VerificationCode;
import br.com.wss.projeto.enums.VerificationType;

@Repository
public interface VerificationCodeRepository extends BaseRepository<VerificationCode, String> {
        @Query(nativeQuery = true, 
                value = "SELECT vc.* FROM verification_code vc "
                + " WHERE a.uid = :account_id AND "
                + " vc.type = CAST(:type AS VARCHAR) "
                + " ORDER BY vc.created_at DESC LIMIT 1"
        )
        Optional<VerificationCode> findLatestActiveCode(final String account_id, final VerificationType type);

        @Modifying
        @Query("UPDATE VerificationCode vc SET vc.revoked = true WHERE vc.account = :account AND vc.type = :type")
        void invalidatePreviousCodes(final Account account, final VerificationType type);
}
