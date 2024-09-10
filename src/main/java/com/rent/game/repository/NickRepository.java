package com.rent.game.repository;


import com.rent.game.model.Nick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NickRepository extends JpaRepository<Nick, Long> {
    List<Nick> findAllByGames_Id(long gameId);
    List<Nick> findByRentedBy(Long rentedBy);
    List<Nick> findByRentalEndBefore(LocalDateTime now);

}
