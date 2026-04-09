package com.algerietelecom.logistics.reservationmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;

public interface UserRepository extends JpaRepository<UserEntitiy, Long> {

}