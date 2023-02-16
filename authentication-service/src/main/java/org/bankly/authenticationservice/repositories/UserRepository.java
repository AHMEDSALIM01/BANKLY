package org.bankly.authenticationservice.repositories;

import org.bankly.authenticationservice.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("**")
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
}
