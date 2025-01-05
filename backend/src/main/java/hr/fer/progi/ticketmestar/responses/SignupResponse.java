package hr.fer.progi.ticketmestar.responses;


import hr.fer.progi.ticketmestar.domain.AppUser;

public class SignupResponse {

    private boolean success;
    private String message;
    private AppUser user;

    public SignupResponse(boolean success, String message, AppUser user){
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
