package com.bisang.backend.Transaction.repository;

import com.bisang.backend.Transaction.domain.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
}
