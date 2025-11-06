package it.gurux.e_shop.service.user;

import it.gurux.e_shop.model.User;
import it.gurux.e_shop.repository.UserRepository;
import it.gurux.e_shop.request.CreateUserRequest;
import it.gurux.e_shop.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return null;
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
