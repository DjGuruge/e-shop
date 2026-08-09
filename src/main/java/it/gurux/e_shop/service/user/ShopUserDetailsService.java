package it.gurux.e_shop.service.user;

import it.gurux.e_shop.model.User;
import it.gurux.e_shop.repository.UserRepository;
import it.gurux.e_shop.service.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = Optional.ofNullable(userRepository.findByEmail(email ))
               .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return ShopUserDetails.buildUserDetail(user);
    }
}
