package app.conferenceroom.db.repository;

import app.conferenceroom.db.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE" +
            "(b.startTime < :endTime AND b.endTime > :startTime) OR " +
            "(b.startTime < :endTime AND b.endTime > :startTime)")
    List<Booking> findAllBookings(@Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);
}
