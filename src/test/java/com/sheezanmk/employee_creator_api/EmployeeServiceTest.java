package com.sheezanmk.employee_creator_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;
import com.sheezanmk.employee_creator_api.entity.Employee;
import com.sheezanmk.employee_creator_api.entity.EmployeeRepository;
import com.sheezanmk.employee_creator_api.entity.EmployeeService;
import com.sheezanmk.employee_creator_api.enums.ContractType;
import com.sheezanmk.employee_creator_api.enums.WorkType;
import com.sheezanmk.employee_creator_api.exceptions.BadRequestException;
import com.sheezanmk.employee_creator_api.exceptions.DuplicateResourceException;
import com.sheezanmk.employee_creator_api.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee validEmployee() {
        Employee e = new Employee();
        e.setFirstName(" Test ");
        e.setLastName(" User ");
        e.setEmail("  TEST@EXAMPLE.COM  ");
        e.setMobileNumber(" +61412345678 ");
        e.setAddress(" Sydney ");
        e.setContractType(ContractType.PERMANENT);
        e.setStartDate(LocalDate.of(2026, 2, 1));
        e.setFinishDate(null);
        e.setOngoing(true);
        e.setWorkType(WorkType.FULL_TIME);
        e.setHoursPerWeek(38);
        return e;
    }

    @Test
    void createEmployee_savesEmployeeWithNormalizedFields() {

        Employee input = validEmployee();

        when(employeeRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(false);

        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee saved = employeeService.createEmployee(input);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());

        Employee persisted = captor.getValue();

        assertEquals("test@example.com", persisted.getEmail());
        assertEquals("Test", persisted.getFirstName());
        assertEquals("User", persisted.getLastName());
        assertEquals("+61412345678", persisted.getMobileNumber());
        assertNotNull(saved);
        assertEquals("test@example.com", saved.getEmail());
    }

    @Test
    void createEmployee_throwsDuplicateResourceException_whenEmailAlreadyExists() {

        Employee input = validEmployee();
        when(employeeRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(input));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_throwsNotFoundException_whenEmployeeDoesNotExist() {

        Long id = 100L;
        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundException.class, () -> employeeService.getEmployeeById(id));
        verify(employeeRepository).findById(id);
    }

    @Test
    void updateEmployee_throwsNotFoundException_whenEmployeeDoesNotExist() {

        Long id = 999L;
        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setFirstName("John");
        dto.setLastName("Smith");
        dto.setEmail("john.smith@example.com");
        dto.setMobileNumber("+61412345678");
        dto.setAddress("Sydney");
        dto.setContractType(ContractType.PERMANENT);
        dto.setStartDate(LocalDate.of(2024, 1, 1));
        dto.setFinishDate(null);
        dto.setOngoing(true);
        dto.setWorkType(WorkType.FULL_TIME);
        dto.setHoursPerWeek(38);

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.updateEmployee(id, dto));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_throwsDuplicateResourceException_whenEmailIsChangedToExistingEmail() {

        Long id = 1L;

        Employee existing = new Employee();
        existing.setId(id);
        existing.setFirstName("Alice");
        existing.setLastName("Smith");
        existing.setEmail("alice@example.com");
        existing.setMobileNumber("+61412345678");
        existing.setAddress("Sydney");
        existing.setContractType(ContractType.PERMANENT);
        existing.setStartDate(LocalDate.of(2024, 1, 1));
        existing.setFinishDate(null);
        existing.setOngoing(true);
        existing.setWorkType(WorkType.FULL_TIME);
        existing.setHoursPerWeek(38);

        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setEmail("bob@example.com");
        dto.setMobileNumber("+61412345678");
        dto.setAddress("Sydney");
        dto.setContractType(ContractType.PERMANENT);
        dto.setStartDate(LocalDate.of(2024, 1, 1));
        dto.setFinishDate(null);
        dto.setOngoing(true);
        dto.setWorkType(WorkType.FULL_TIME);
        dto.setHoursPerWeek(38);

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.of(existing));
        when(employeeRepository.existsByEmailIgnoreCase("bob@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.updateEmployee(id, dto));

        verify(employeeRepository, never()).save(any(Employee.class));

    }

    @Test
    void patchEmployee_setsFinishDateToNull_whenOngoingIsTrue() {

        Long id = 1L;

        Employee existing = new Employee();
        existing.setId(id);
        existing.setFirstName("Alice");
        existing.setLastName("Smith");
        existing.setEmail("alice@example.com");
        existing.setMobileNumber("+61412345678");
        existing.setAddress("Sydney");
        existing.setContractType(ContractType.CONTRACT);
        existing.setStartDate(LocalDate.of(2024, 1, 1));
        existing.setFinishDate(LocalDate.of(2025, 1, 1));
        existing.setOngoing(false);
        existing.setWorkType(WorkType.FULL_TIME);
        existing.setHoursPerWeek(38);

        PatchEmployeeDto dto = new PatchEmployeeDto();
        dto.setOngoing(true); //

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.of(existing));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee saved = employeeService.patchEmployee(id, dto);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());

        Employee persisted = captor.getValue();

        assertTrue(persisted.isOngoing());
        assertNull(persisted.getFinishDate());
        assertNull(saved.getFinishDate());
    }

    @Test
    void patchEmployee_throwsBadRequestException_whenSettingPartTimeWithoutHours() {

        Long id = 1L;

        Employee existing = new Employee();
        existing.setId(id);
        existing.setFirstName("Alice");
        existing.setLastName("Smith");
        existing.setEmail("alice@example.com");
        existing.setMobileNumber("+61412345678");
        existing.setAddress("Sydney");
        existing.setContractType(ContractType.PERMANENT);
        existing.setStartDate(LocalDate.of(2024, 1, 1));
        existing.setFinishDate(null);
        existing.setOngoing(true);
        existing.setWorkType(WorkType.FULL_TIME);
        existing.setHoursPerWeek(38);

        PatchEmployeeDto dto = new PatchEmployeeDto();
        dto.setWorkType(WorkType.PART_TIME);

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.of(existing));
        assertThrows(BadRequestException.class, () -> employeeService.patchEmployee(id, dto));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_throwsNotFoundException_whenEmployeeDoesNotExist() {
        Long id = 10L;
        when(employeeRepository.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> employeeService.deleteEmployee(id));
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteEmployee_deletesEmployee_whenEmployeeExists() {

        Long id = 1L;
        Employee existing = new Employee();
        existing.setId(id);
        when(employeeRepository.existsById(id)).thenReturn(true);
        employeeService.deleteEmployee(id);
        verify(employeeRepository).deleteById(id);
    }
}
