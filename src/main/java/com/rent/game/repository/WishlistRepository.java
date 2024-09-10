package com.rent.game.repository;

import com.rent.game.model.Account;
import com.rent.game.model.Game;
import com.rent.game.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByAccountAndGames(Account account, Game game);

    Wishlist findByAccount_Id(Long accountId);

    Set<Wishlist> findByGames(Game game);
}
