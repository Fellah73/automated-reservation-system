package com.algerietelecom.logistics.reservationmanager.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findAllByEmail(String email);
}