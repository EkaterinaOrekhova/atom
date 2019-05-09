package ru.atom.chat.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "message", schema = "chat")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    //@Column(name = "user", unique = true, nullable = false, length = 20)
    private User user;

    @Column(name = "time", nullable = false, length = 20)
    private Date time = new Date();

    @Column(name = "value", nullable = false, length = 20)
    private String value;

    public User getUser() {
        return user;
    }

    public Message setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Message setTime(Date timestamp) {
        this.time = timestamp;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Message setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
            "user=" + user +
            ", timestamp=" + time +
            ", value='" + value + '\'' +
            '}';
    }
}