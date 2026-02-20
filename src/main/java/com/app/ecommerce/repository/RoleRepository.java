package com.app.ecommerce.repository;

import com.app.ecommerce.model.AppRole;
import com.app.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

   Optional<Role> findByRoleName(AppRole appRole);
}
