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
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomId = :roomId " +
            "AND ((b.startTime < :endTime AND b.endTime > :startTime) OR " +
            "     (b.startTime < :endTime AND b.endTime > :startTime))")
    boolean hasOverlap(@Param("roomId") Long roomId,
                       @Param("startTime") LocalDateTime startTime,
                       @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomId in (:roomIds) " +
            "AND ((b.startTime < :endTime AND b.endTime > :startTime) OR " +
            "     (b.startTime < :endTime AND b.endTime > :startTime))")
    boolean hasOverlap(@Param("roomIds") List<Long> roomIds,
                       @Param("startTime") LocalDateTime startTime,
                       @Param("endTime") LocalDateTime endTime);
}
