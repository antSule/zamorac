package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;


@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> user = repository.findByUsername(username);
        if(user.isPresent()){
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername()).password(userObj.getPassword()).build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }



    /*
    @Value("${progi.admin.password}")
    private String adminPasswordHash;

    @Override
    public UserDetails loadUserByUsername(String username){
        if ("admin".equals(username))
            return new User(username, adminPasswordHash, commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        else throw new UsernameNotFoundException("No user " + username);
    }


     */
}
