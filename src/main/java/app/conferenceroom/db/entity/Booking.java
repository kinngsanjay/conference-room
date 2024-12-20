package app.conferenceroom.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "BOOKING")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKINGID")
    private Long bookingId;
    @Column(name = "ROOMID")
    private Long roomId;
    @Column(name = "START_TIME")
    private LocalTime startTime;
    @Column(name = "END_TIME")
    private LocalTime endTime;
    @Column(name = "NUMBER_OF_PEOPLE")
    private int numberOfPeople;
}
