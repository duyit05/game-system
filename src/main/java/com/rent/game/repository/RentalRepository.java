package com.rent.game.repository;


import com.rent.game.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByNickId(long userId);

    // You can add more custom query methods if needed
}