package com.app.orderapi.repository;

import com.app.orderapi.entities.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {

    Confirmation findByToken(String token);
    Confirmation findByUserId(Long id);

}
