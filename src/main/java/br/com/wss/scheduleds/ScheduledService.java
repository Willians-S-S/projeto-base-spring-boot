package br.com.wss.scheduleds;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.wss.projeto.business.AccountBusiness;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ScheduledService {

    private final AccountBusiness accountBusiness;

    @Scheduled(fixedRateString = "PT12H")
    public void permanentlyDeleteAccounts() {
        accountBusiness.permanentlyDelete();
    }
}
