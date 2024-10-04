package com.rent.game.service;

import com.rent.game.model.Account;
import com.rent.game.model.Game;
import com.rent.game.model.Wishlist;
import com.rent.game.repository.AccountRepository;
import com.rent.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WishlistService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GameRepository gameRepository;

    public void addToWishlist(Long accountId, int gameId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        Game game = gameRepository.findById(gameId).orElse(null);

        if (account != null && game != null) {
            Wishlist wishList = account.getWishList();
            if (wishList == null) {
                wishList = new Wishlist();
                wishList.setAccount(account);
                wishList.setCreatedDate(new Date());
                account.setWishList(wishList);
            }

            boolean productAlreadyInWishlist = wishList.getGames().contains(game);

            if (!productAlreadyInWishlist) {
                wishList.addGame(game);
                accountRepository.save(account);
            }
        }
    }
}
