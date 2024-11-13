package hr.fer.progi.ticketmestar.rest;
import hr.fer.progi.ticketmestar.domain.AppUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityBasic {

    @Value("${progi.fronted.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(formLogin -> formLogin
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                })
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
        );

        http.oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("http://localhost:3000/home");
                })
        );

        http.httpBasic(withDefaults());

        http.exceptionHandling(configurer -> {
            final RequestMatcher nonHtmlRequests = new NegatedRequestMatcher(new MediaTypeRequestMatcher(MediaType.TEXT_HTML));
            configurer.defaultAuthenticationEntryPointFor(
                    new Http403ForbiddenEntryPoint(), nonHtmlRequests
            );
        });


        http.logout(logout -> logout
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "SESSION", "XSRF-TOKEN","access_token")
                .logoutSuccessUrl("http://localhost:3000")
        );

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    private GrantedAuthoritiesMapper authorityMapper() {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setDefaultAuthority("ROLE_ADMIN");
        return authorityMapper;
    }


    private final AppUserService appUserService;
    private final ConcertService concertService;

    @Autowired
    public WebSecurityBasic(AppUserService appUserService, ConcertService concertService) {
        this.appUserService = appUserService;
        this.concertService = concertService;
    }

    @Bean
    public AuthenticationProvider appUserAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider concertAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(concertService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(appUserAuthenticationProvider(), concertAuthenticationProvider());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (exception instanceof BadCredentialsException) {
                response.setStatus(401);  // Set status to Unauthorized if credentials are invalid
                response.getWriter().write("{\"error\":\"Invalid credentials\"}");  // Custom error message
            } else {
                response.setStatus(400);  // Set status for other failures (400 Bad Request)
                response.getWriter().write("{\"error\":\"Login failed\"}");  // Generic failure message
            }
        };
    }



}