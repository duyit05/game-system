package com.rent.game.controller;

import com.rent.game.dto.AccountDTO;
import com.rent.game.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rent-game/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping(path = "/top3")
    public ResponseEntity<List<AccountDTO>> getTop3Account_Balance() {
        return ResponseEntity.ok(accountService.getTop3AccountsByBalance());
    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDTO));
    }
}
