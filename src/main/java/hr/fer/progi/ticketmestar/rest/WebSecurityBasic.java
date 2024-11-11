package hr.fer.progi.ticketmestar.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity
public class WebSecurityBasic {

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager()) // Set custom AuthenticationManager
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/registration/**").permitAll();
                    registry.requestMatchers("/concerts/all").permitAll();
                    registry.requestMatchers("/h2-console/**").permitAll();
                    registry.requestMatchers("/concerts/add").permitAll();
                    registry.requestMatchers("/getuser/**").permitAll();
                    registry.requestMatchers("/allusers/**").permitAll();
                    registry.requestMatchers("/callback").permitAll();
                    registry.requestMatchers("/error/**").permitAll();
                    registry.requestMatchers("/search/**").permitAll();
                    registry.requestMatchers("/home/**").permitAll();
                    registry.requestMatchers("/albums/**").permitAll();
                    registry.requestMatchers("/following/**").permitAll();
                    registry.requestMatchers("/me/**").permitAll();
                    registry.requestMatchers("/login/**").permitAll();
                    registry.requestMatchers("/oauth2/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureHandler(authenticationFailureHandler())
                        .defaultSuccessUrl("/", true))
                .oauth2Login(form -> form
                        .defaultSuccessUrl("http://localhost:3000")
                        .failureUrl("/error"))
                .anonymous(anon -> anon.authorities("ROLE_ANONYMOUS"))
                .build();
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

    /*
    @Value("${progi.fronted.url}")
    private String frontendUrl;


    @Bean
    @Profile("basic-security")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.oauth2Login(withDefaults());
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Profile("oauth-security")
    public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                            .userInfoEndpoint(
                                    userInfoEndpoint -> userInfoEndpoint.userAuthoritiesMapper(this.authorityMapper()))
                            .successHandler(
                                    (request, response, authentication) -> {
                                        response.sendRedirect(frontendUrl);
                                    });
                })
                .exceptionHandling(handling -> handling.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .build();
    }


    @Bean
    @Profile("form-security")
    public SecurityFilterChain spaFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .anyRequest().authenticated());
        http.formLogin(configurer -> {
                    configurer.successHandler((request, response, authentication) ->
                                    response.setStatus(HttpStatus.NO_CONTENT.value())
                            )
                            .failureHandler(new SimpleUrlAuthenticationFailureHandler());
                }
        );
        http.exceptionHandling(configurer -> {
            final RequestMatcher matcher = new NegatedRequestMatcher(
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML));
            configurer
                    .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }, matcher);
        });
        http.logout(configurer -> configurer
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) ->
                        response.setStatus(HttpStatus.NO_CONTENT.value())));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }




    @Bean
    @Profile({ "basic-security", "form-security" })
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PathRequest.toH2Console());
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }



    private GrantedAuthoritiesMapper authorityMapper() {
        final SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();

        authorityMapper.setDefaultAuthority("ROLE_ADMIN");

        return authorityMapper;
    }



*/

}