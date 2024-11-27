package com.api.tika.repository;


import com.api.tika.models.USER.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    Optional<Token> getByToken(String token);

   Optional <Token> findByToken(String token);
}
