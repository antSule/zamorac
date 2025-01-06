package hr.fer.progi.ticketmestar.authControllers;

import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.Role;
import hr.fer.progi.ticketmestar.rest.ConcertService;
import hr.fer.progi.ticketmestar.service.impl.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ConcertService concertService;

    public AdminController(AdminService adminService, ConcertService concertService){
        this.adminService = adminService;
        this.concertService = concertService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> listAllUsers(){
        return ResponseEntity.ok(adminService.allUsers());
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId){
        try{
            adminService.deleteUser(userId);
            return ResponseEntity.ok(adminService.allUsers());
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> changeUserRoles(@RequestParam Long userId, @RequestParam Set<Role> roles){
        try{
            AppUser updatedUser = adminService.changeUserRoles(userId, roles);
            return ResponseEntity.ok(updatedUser);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/remove")
    public ResponseEntity<?> deleteConcert(@RequestParam Long concertId){
        try{
            adminService.deleteConcert(concertId);
            return ResponseEntity.ok(concertService.concertList());
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}