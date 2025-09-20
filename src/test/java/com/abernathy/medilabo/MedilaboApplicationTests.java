package com.abernathy.medilabo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MedilaboApplicationTests {

	@Test
	void contextLoads() {
	}

//	@Test
//	void main() {
//		// Directly invoke the main method
//		MedilaboApplication.main(new String[] {});
//	}

}
