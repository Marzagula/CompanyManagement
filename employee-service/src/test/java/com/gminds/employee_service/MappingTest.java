package com.gminds.employee_service;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MappingTest {

    @Test
    public void testMappingEmployeeToEmployeeDTO() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test");
        employee.setSurname("User");
        // Ustaw inne właściwości jeśli potrzebne

        EmployeeDTO employeeDTO = EmployeeMapper.INSTANCE.toEmployeeDTO(employee);

        assertNotNull(employeeDTO.id());
        assertEquals(1L, employeeDTO.id());
        assertEquals("Test", employeeDTO.name());
        assertEquals("User", employeeDTO.surname());
        // Dodatkowe asercje na inne właściwości
    }
}
