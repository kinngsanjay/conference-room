package app.conferenceroom.db.repository;

import app.conferenceroom.db.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.capacity >= :capacity")
    List<Room> findRoomByCapacity(@Param("capacity") int capacity);

    Room findByName(String name);

}
