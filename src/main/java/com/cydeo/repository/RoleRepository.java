package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { //Entity koyduk Role yazarak
    //build all queries that will bring data
    //repsitory CRUD
    //derive,@Query(JPA-Native)
    //yukaridakilar extend ile hallolur
}
