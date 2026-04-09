package com.algerietelecom.logistics.reservationmanager.repositories;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<UserEntitiy, Long> {
    UserEntitiy findAllByEmail(String email);
}