package javaweb.shopping_cart.service;


import javaweb.shopping_cart.entity.UserEntity;

import java.util.Optional;

public interface UserEntityService {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    void createNewUserEntity(UserEntity userEntity);
}
