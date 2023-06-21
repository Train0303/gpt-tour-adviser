package com.gptTour.backEnd.repository;

import com.gptTour.backEnd.entity.Account;
import com.gptTour.backEnd.exception.CustomException;
import com.gptTour.backEnd.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountRepository {
    private final EntityManager em;

    public Optional<Account> findByEmail(String email){
        List<Account> account = em.createQuery("select a from Account a where a.email= :email", Account.class)
                .setParameter("email", email)
                .getResultList();
        return account.stream().findAny();
    }

    public Long save(Account account){
        if (findByEmail(account.getEmail()).isEmpty()){
            em.persist(account);
            return account.getId();
        }
        return -1L;
    }
}
