package com.zamorac.ticketmestar.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@CrossOrigin(origins = "http://localhost:3000/")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/add")
    public String add(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

}
