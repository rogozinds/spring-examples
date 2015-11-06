package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by dmitrii on 06/11/15.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findByUsername(String username);

}
