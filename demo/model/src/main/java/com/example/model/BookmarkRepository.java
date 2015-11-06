package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dmitrii on 06/11/15.
 */
public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{

    public Bookmark findByAccountUsername(String username);
}
