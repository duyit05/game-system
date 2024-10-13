package com.rent.game.controller;

import com.rent.game.dto.GameDTO;
import com.rent.game.repository.CategoryRepository;
import com.rent.game.repository.GameImageRepository;
import com.rent.game.repository.GameRepository;
import com.rent.game.repository.GameVideoRepository;
import com.rent.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/rent-game/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameImageRepository gameImageRepository;

    @Autowired
    private GameVideoRepository gameVideoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/home")
    public List<GameDTO> getAllGames() {
        return gameService.getAllGamesHome();
    }

    // Inside GameController class
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable("id") long gameId) {
        try {
            Optional<GameDTO> game = gameService.getGameById(gameId);
            return game.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getRandomKeywords() {
        List<String> allGameNames = gameService.getAllGameNames();

        List<String> keywords = getRandomKeywordsFromNames(allGameNames);

        return new ResponseEntity<>(keywords, HttpStatus.OK);
    }

    private List<String> getRandomKeywordsFromNames(List<String> gameNames) {
        Set<String> uniqueKeywords = new HashSet<>();
        Random random = new Random();
        int numOfKeywords = random.nextInt(3) + 5;

        for (String name : gameNames) {
            uniqueKeywords.add(name);
        }


        List<String> result = new ArrayList<>();
        while (result.size() < numOfKeywords && !uniqueKeywords.isEmpty()) {
            int randomIndex = random.nextInt(uniqueKeywords.size());
            Iterator<String> iterator = uniqueKeywords.iterator();
            String keyword = null;
            for (int i = 0; i <= randomIndex; i++) {
                keyword = iterator.next();
            }
            result.add(keyword);
            uniqueKeywords.remove(keyword);
        }

        return result;
    }

    @GetMapping("/random")
    public List<GameDTO> getRandomGames() {
        List<GameDTO> allGames = gameService.getAllGamesHome();
        Set<Integer> chosenIndices = new HashSet<>();
        List<GameDTO> randomGames = new ArrayList<>();
        Random random = new Random();

        while (randomGames.size() < 3) {
            int randomIndex = random.nextInt(allGames.size());
            if (!chosenIndices.contains(randomIndex)) {
                randomGames.add(allGames.get(randomIndex));
                chosenIndices.add(randomIndex);
            }
        }

        return randomGames;
    }

}
