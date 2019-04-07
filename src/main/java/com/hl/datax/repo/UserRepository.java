package com.hl.datax.repo;

import com.hl.datax.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
  User findUserByUserNameAndPassword(String userName, String password);

  User findUserByUserName(String username);

  @Transactional
  void deleteByUserName(String userName);
}
