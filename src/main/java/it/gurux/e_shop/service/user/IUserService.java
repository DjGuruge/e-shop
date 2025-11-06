package it.gurux.e_shop.service.user;

import it.gurux.e_shop.model.User;

import it.gurux.e_shop.request.CreateUserRequest;
import it.gurux.e_shop.request.UserUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserService extends JpaRepository<User,Long> {

    User getUserById (Long userId);
    User createUser (CreateUserRequest request);
    User updateUser (UserUpdateRequest request, Long userId);
    void deleteUser (Long userId);

}
