package com.devwiki.leafy.repository.userPlant;

import com.devwiki.leafy.model.user.User;
import com.devwiki.leafy.model.userPlant.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPlantRepository extends JpaRepository<UserPlant, Long> {
    List<UserPlant> findAllByUser(User user);
}
