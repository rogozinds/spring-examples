package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by dmitrii on 06/11/15.
 */
public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{

    public Collection<Bookmark> findByAccountUsername(String username);
}
