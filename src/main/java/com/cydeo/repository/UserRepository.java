package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    //drive query
    User findByUserName(String username);
@Transactional
// buraya koymaktansa userService implamentation classs a koyduk
    //MUTLAKA IHTIYAC VAR DELETE METHOD ICIN BU ANNOTATION A
    void deleteByUserName(String username);
  //manager verisesm menager retun olacak admin verrssm emmin gelecek
    List<User> findAllByRoleDescriptionIgnoreCase(String description);
    // Manager create edersem UserCreate sayfasindan onu Project page sayfasinda
    // gorebiliyorum.
}
