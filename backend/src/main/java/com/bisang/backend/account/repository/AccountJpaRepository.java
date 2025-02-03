package com.bisang.backend.account.repository;

import com.bisang.backend.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, String> {
   default Account createAccount(Account account);
}
