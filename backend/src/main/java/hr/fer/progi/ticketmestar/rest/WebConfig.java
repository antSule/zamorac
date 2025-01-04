package hr.fer.progi.ticketmestar.rest;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebConfig {

    private final AppUserRepository appUserRepository;

    public WebConfig(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Bean
    UserDetailsService userDetailsService(){
        return username -> appUserRepository.findByEmailAndAuthenticationProvider(username, hr.fer.progi.ticketmestar.domain.AuthenticationProvider.JWT)
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

}

