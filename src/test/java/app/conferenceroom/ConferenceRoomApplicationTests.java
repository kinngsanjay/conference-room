package app.conferenceroom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class ConferenceRoomApplicationTests {

	@Test
	void contextLoads() {
        System.out.println("Context Loads");
	}

}
