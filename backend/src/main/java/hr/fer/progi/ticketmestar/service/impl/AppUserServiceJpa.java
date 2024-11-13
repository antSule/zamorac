package hr.fer.progi.ticketmestar.service.impl;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class AppUserServiceJpa implements AppUserDetailsService {

    @Autowired
    private AppUserRepository appUserRepo;

    @Override
    public List<AppUser> listAll() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser createAppUser(AppUser appUser) {
        Assert.notNull(appUser, "AppUser object must be given");
        Assert.isNull(appUser.getId(), "AppUser ID must be null, not" + appUser.getId());
        return appUserRepo.save(appUser);
    }
}
