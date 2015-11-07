package com.example;


import com.example.model.Account;
import com.example.model.AccountRepository;
import com.example.model.Bookmark;
import com.example.model.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class DemoApplication {


    @Bean
    CommandLineRunner init(AccountRepository accountRepository,
                           BookmarkRepository bookmarkRepository) {
        return (evt) -> Arrays.asList(
                "jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
                .forEach(
                        a -> {
                            Account account = accountRepository.save(new Account(a,"password"));
                            bookmarkRepository.save(new Bookmark(account,
                                    "http://bookmark.com/1/" + a, "A description"));
                            bookmarkRepository.save(new Bookmark(account,
                                    "http://bookmark.com/2/" + a, "A description"));
                        });
    }

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    @RequestMapping("/{userId}/bookmarks")
    class BookmarkRestController {

        private final BookmarkRepository bookmarkRepository;
        private final AccountRepository accountRepository;


        @RequestMapping(method = RequestMethod.POST)
        ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input) {
            return accountRepository.findByUsername(userId).map(
                    account -> {
                        Bookmark result = bookmarkRepository.save(new Bookmark(account, input.uri, input.description));

                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                                .buildAndExpand(result.id)
                                .toUri());
                        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                    }
            ).orElseThrow(() -> new RuntimeException("could not find the user '" + userId + "'"));

        }

        @RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
        Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
            return this.bookmarkRepository.findOne(bookmarkId);
        }

        @RequestMapping(method = RequestMethod.GET)
        Collection<Bookmark> readBookmarks(@PathVariable String userId) {
            return bookmarkRepository.findByAccountUsername(userId);
        }

        @Autowired
        BookmarkRestController(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
            this.bookmarkRepository = bookmarkRepository;
            this.accountRepository = accountRepository;
        }
    }


}
