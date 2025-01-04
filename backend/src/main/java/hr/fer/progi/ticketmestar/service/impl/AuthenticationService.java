package hr.fer.progi.ticketmestar.service.impl;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Role;
import hr.fer.progi.ticketmestar.dto.LoginUserDto;
import hr.fer.progi.ticketmestar.dto.RegisterUserDto;
import hr.fer.progi.ticketmestar.dto.VerifyUserDto;
import hr.fer.progi.ticketmestar.responses.SignupResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    public SignupResponse signUp(RegisterUserDto input){

        Optional<AppUser> existingUser = appUserRepository.findByEmailAndAuthenticationProvider(input.getEmail(), AuthenticationProvider.JWT);
        if(existingUser.isPresent()){
            return new SignupResponse(false, "Email is already taken.", null);
        }

        Optional<AppUser> existingUser2 = appUserRepository.findByUsernameAndAuthenticationProvider(input.getUsername(), AuthenticationProvider.JWT);
        if(existingUser2.isPresent()){
            return new SignupResponse(false, "Username is already taken.", null);
        }

        Set<Role> roles = input.getRolesAsEnum();

        AppUser user = new AppUser(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), AuthenticationProvider.JWT, roles.toArray(new Role[0]));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        appUserRepository.save(user);

        return new SignupResponse(true, "User registered successfully.", user);
    }


    public AppUser authenticate(LoginUserDto input){
        AppUser user = appUserRepository.findByEmailAndAuthenticationProvider(input.getEmail(), AuthenticationProvider.JWT).orElseThrow(()-> new RuntimeException("User not found."));

        if(!user.isEnabled()){
            throw new RuntimeException("Account is not verified.");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        return user;
    }

    public void verifyUser(VerifyUserDto input){
        Optional<AppUser> optionalUser =appUserRepository.findByEmailAndAuthenticationProvider(input.getEmail(), AuthenticationProvider.JWT);
        if(optionalUser.isPresent()){
            AppUser user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code has expired.");
            }
            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                appUserRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code.");
            }
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    public void resendVerificationCode(String email){
        Optional<AppUser> optionalUser = appUserRepository.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.JWT);
        if(optionalUser.isPresent()){
            AppUser user = optionalUser.get();

            if(user.isEnabled()){
                throw new RuntimeException("Account is already verified.");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            appUserRepository.save(user);
        } else {
            throw new RuntimeException("User is not found.");
        }
    }

    public void sendVerificationEmail(AppUser user){
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch(MessagingException e){
            e.printStackTrace();
        }
    }

    public String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
