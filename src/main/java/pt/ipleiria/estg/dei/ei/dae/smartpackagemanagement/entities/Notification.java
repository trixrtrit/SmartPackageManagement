package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification extends Versionable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "username")
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @OneToOne
    @NotNull
    @JoinColumn(name = "measurement_id", referencedColumnName = "id")
    private Measurement measurement;

    public Notification() {
    }

    public Notification(String text, User user, Measurement measurement) {
        this.text = text;
        this.user = user;
        this.measurement = measurement;
        this.timestamp = new Date();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
}
