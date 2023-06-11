package com.wallet.wallet.model.repo;

import com.wallet.wallet.model.UserObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserObj, Long> {
    UserObj findByUsername(String username);

}
