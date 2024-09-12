package com.rent.game.service;

import com.rent.game.dto.AccountDTO;
import com.rent.game.model.Account;

import java.util.List;

public interface AccountService {
    Account getAccount(Long accountId);

    List<AccountDTO> getListAccount();

    List<AccountDTO> getTop3AccountsByBalance();

    AccountDTO getAccountById(Long accountId);

    AccountDTO updateAccount(Long accountId, AccountDTO accountDTO);
}
