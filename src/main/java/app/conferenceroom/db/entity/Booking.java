package app.conferenceroom.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "BOOKING")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long bookingId;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    @ToString.Exclude
    private Room room;
    @Column(name = "BOOKING_REFERENCE")
    private String bookingReference;
    @Column(name = "MEETING_DATE")
    private LocalDate meetingDate;
    @Column(name = "START_TIME")
    private LocalTime startTime;
    @Column(name = "END_TIME")
    private LocalTime endTime;
    @Column(name = "NUMBER_OF_PEOPLE")
    private int numberOfPeople;
}
