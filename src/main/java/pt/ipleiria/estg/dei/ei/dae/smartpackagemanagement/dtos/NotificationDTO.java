package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

public class NotificationDTO implements Serializable {
    private Long id;
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private UserDTO user;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, String text, Date timestamp, UserDTO user) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
