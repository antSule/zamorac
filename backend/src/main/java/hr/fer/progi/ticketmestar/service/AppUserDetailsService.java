package hr.fer.progi.ticketmestar.service;

import hr.fer.progi.ticketmestar.domain.AppUser;

import java.util.List;

public interface AppUserDetailsService {
    List<AppUser> listAll();


    /**
     * Creates new appUser in the system
     * @param appUser object to create, with ID set to null
     * @return created appUser object in the system with ID set
     * @throws IllegalArgumentException if given appUser is null or its ID is not null
     */
   AppUser createAppUser(AppUser appUser);
}
