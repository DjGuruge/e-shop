package it.gurux.e_shop.service.user;

import it.gurux.e_shop.dto.UserDto;
import it.gurux.e_shop.exception.AlreadyExistException;
import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.User;
import it.gurux.e_shop.repository.UserRepository;
import it.gurux.e_shop.request.CreateUserRequest;
import it.gurux.e_shop.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user-> !userRepository.existsByEmail(request.getEmail()))
                .map(req-> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistException(request.getEmail()+"already exist"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser ->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);

        }).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()->
        {throw new ResourceNotFoundException("User not found");});

    }

    @Override
    public UserDto convertUSerToDto(User user){
        return modelMapper.map(user, UserDto.class);

    }

    @Override
    public User getAuthenticatedUser() {
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return  userRepository.findByEmail(getAuthenticatedUser().getEmail());
    }

}
