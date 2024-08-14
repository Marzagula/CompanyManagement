package com.gminds.auth_server;

import com.gminds.auth_server.model.EmployeeUser;
import com.gminds.auth_server.repository.EmployeeUserRepository;
import com.gminds.auth_server.service.EmployeeUserDetailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServerApplicationTests {

	@Mock
	private EmployeeUserRepository employeeUserRepository;

	@InjectMocks
	private EmployeeUserDetailService employeeUserDetailService;

	public AuthServerApplicationTests() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testLoadUserByUsername_UserExists() {
		// setup
		String username = "testuser";
		EmployeeUser testUser = new EmployeeUser(1L, username, "password123", "ROLE_USER");
		when(employeeUserRepository.findByUsername(username)).thenReturn(testUser);

		// execute
		UserDetails userDetails = employeeUserDetailService.loadUserByUsername(username);

		// verify
		assertNotNull(userDetails);
		assertEquals(username, userDetails.getUsername());
	}

	@Test
	public void testLoadUserByUsername_UserDoesNotExist() {
		// setup
		String username = "nonexistentuser";
		when(employeeUserRepository.findByUsername(username)).thenReturn(null);

		// execute and verify
		assertThrows(UsernameNotFoundException.class, () -> {
			employeeUserDetailService.loadUserByUsername(username);
		});
	}


}
