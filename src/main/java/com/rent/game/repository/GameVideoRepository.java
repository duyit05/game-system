package com.rent.game.repository;


import com.rent.game.model.Game;
import com.rent.game.model.Game_video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameVideoRepository extends JpaRepository<Game_video, Long> {

    List<Game_video> findByGame(Game game);

}
