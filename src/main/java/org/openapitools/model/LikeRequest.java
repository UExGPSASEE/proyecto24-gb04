package org.openapitools.model;

public class LikeRequest {
    private String videotitle;
    private Long userId;

    public LikeRequest(String videotitle, Long userId) {
        this.videotitle = videotitle;
        this.userId = userId;
    }

    // Getters y setters
    public String getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(String videotitle) {
        this.videotitle = videotitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

