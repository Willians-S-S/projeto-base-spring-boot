package br.com.wss.projeto.business;

import br.com.wss.base.BaseBusiness;
import br.com.wss.projeto.entities.Account;
import br.com.wss.projeto.entities.VerificationCode;
import br.com.wss.projeto.enums.VerificationType;

public interface VerificationCodeBusiness extends BaseBusiness<VerificationCode, String> {

    void sendVerificationCode(Account account, VerificationType type);

    void validateCode(Account account, String inputCode, VerificationType type);

}
