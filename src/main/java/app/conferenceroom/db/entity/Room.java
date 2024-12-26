package app.conferenceroom.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ROOM")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Column(name = "NAME", length = 14)
    private String name;
    @Column(name = "CAPACITY", length = 14)
    private int capacity;
}
