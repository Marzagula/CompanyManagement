package com.gminds.employee_service;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeeMapperTest {

    @Test
    public void shouldMapEmployeeToEmployeeDTO() {
        Employee employee = new Employee();
        employee.setName("John");
        employee.setSurname("Doe");
        employee.setId(1L);

        EmployeeDTO employeeDTO = EmployeeMapper.INSTANCE.toEmployeeDTO(employee);

        assertNotNull(employeeDTO);
        assertEquals(employee.getName(), employeeDTO.name());
        assertEquals(employee.getSurname(), employeeDTO.surname());
        assertEquals(employee.getId(), employeeDTO.id());
    }

    @Test
    public void shouldMapEmployeeDTOToEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John", "Doe", null, null, null, null, null);

        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);

        assertNotNull(employee);
        assertEquals(employeeDTO.name(), employee.getName());
        assertEquals(employeeDTO.surname(), employee.getSurname());
        assertEquals(employeeDTO.id(), employee.getId());
    }
}
