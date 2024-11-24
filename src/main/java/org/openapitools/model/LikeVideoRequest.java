package org.openapitools.model;

public class LikeVideoRequest {
    private String videotitle;
    private String username;
    private Long idUserSession;

    // Getters y setters
    public String getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(String videotitle) {
        this.videotitle = videotitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIdUserSession() {
        return idUserSession;
    }

    public void setIdUserSession(Long idUserSession) {
        this.idUserSession = idUserSession;
    }
}
