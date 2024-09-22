package com.rent.game.service;

import com.rent.game.dto.RentalDTO;
import com.rent.game.exception.*;
import com.rent.game.model.Account;
import com.rent.game.model.Game;
import com.rent.game.model.Nick;
import com.rent.game.model.Rental;
import com.rent.game.repository.AccountRepository;
import com.rent.game.repository.GameRepository;
import com.rent.game.repository.NickRepository;
import com.rent.game.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;
    private final NickRepository nickRepository;
    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository, NickRepository nickRepository, GameRepository gameRepository, AccountRepository accountRepository) {
        this.rentalRepository = rentalRepository;
        this.nickRepository = nickRepository;
        this.gameRepository = gameRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public RentalDTO rentAccountByHour(long userId, long gameId, int hours) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        List<Nick> gameAccounts = findAvailableGameAccounts(gameId);

        if (gameAccounts.isEmpty()) {
            throw new GameAccountNotFoundException("No game account found for the requested game");
        }

        if (game.getStock() <= 0) {
            throw new OutOfStockException("Game is out of stock");
        }
        logger.info("pass stock");
        if (checkIfAccountIsInUseByAnotherUser(gameAccounts, userId)) {
            throw new GameAccountInUseException("Game account is currently in use by another user");
        }
        logger.info("pass check acc was used");

        if (account.getBalance() < game.getPrice() * hours) {
            throw new InsufficientBalanceException("Insufficient balance to rent the game for specified hours");
        }
        logger.info("pass check balance");

        RentalDTO rentalDTO;
        if (isAccountRentedByCurrentUser(gameAccounts, userId)) {
            logger.info("hello extend");

            rentalDTO = extendRentalTimeForCurrentUser(gameAccounts, userId, hours);
        } else {
            logger.info("hello rent new");

            rentalDTO = rentNewAccountForCurrentUser(account, gameAccounts, game, userId, hours);
        }

        return rentalDTO;
    }

    private List<Nick> findAvailableGameAccounts(long gameId) {
        return nickRepository.findAllByGames_Id(gameId);
    }

    private boolean checkIfAccountIsInUseByAnotherUser(List<Nick> gameAccounts, Long userId) {
        return gameAccounts.stream()
                .anyMatch(gameAccount ->
                        "Rented".equals(gameAccount.getStatus()) &&
                                (gameAccount.getRentedBy() == null || !gameAccount.getRentedBy().equals(userId))
                );
    }

    private boolean isAccountRentedByCurrentUser(List<Nick> gameAccounts, long userId) {
        return gameAccounts.stream()
                .anyMatch(gameAccount -> gameAccount.getRentedBy() != null && gameAccount.getRentedBy().equals(userId));
    }

    private RentalDTO extendRentalTimeForCurrentUser(List<Nick> gameAccounts, long userId, int hours) {
        Nick selectedGameAccount = gameAccounts.stream()
                .filter(gameAccount -> gameAccount.getRentedBy().equals(userId))
                .findFirst()
                .orElseThrow(() -> new GameAccountNotFoundException("Game account not found"));

        LocalDateTime newRentalEnd = selectedGameAccount.getRentalEnd().plusHours(hours);
        selectedGameAccount.setRentalEnd(newRentalEnd);
        nickRepository.save(selectedGameAccount);

        logger.info("Game rental time extended successfully!");
        return convertToRentalDTO(selectedGameAccount.getRental());
    }

    private RentalDTO rentNewAccountForCurrentUser(Account account, List<Nick> gameAccounts, Game game, long userId, int hours) {
        Random random = new Random();
        Nick selectedGameAccount = gameAccounts.get(random.nextInt(gameAccounts.size()));
        selectedGameAccount.setStatus("Rented");
        selectedGameAccount.setRentedBy(userId);
        selectedGameAccount.setRentalStart(LocalDateTime.now());
        selectedGameAccount.setRentalEnd(LocalDateTime.now().plusHours(hours));
        nickRepository.save(selectedGameAccount);

        // Deduct user's balance
        account.setBalance(account.getBalance() - game.getPrice() * hours);
        accountRepository.save(account);

        // Create new rental
        Rental rental = createRental(selectedGameAccount, game);
        logger.info("Gamerented successfully!");
        return convertToRentalDTO(rental);
    }
}
