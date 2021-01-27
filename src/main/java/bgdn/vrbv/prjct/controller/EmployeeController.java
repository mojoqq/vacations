package bgdn.vrbv.prjct.controller;

import bgdn.vrbv.prjct.config.EmployeeVacationsToExcelConfig;
import bgdn.vrbv.prjct.controller.generated.api.EmployeeApi;
import bgdn.vrbv.prjct.entity.Employee;
import bgdn.vrbv.prjct.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rbgdn.vrbv.prjct.controller.generated.model.EmployeeDto;
import rbgdn.vrbv.prjct.controller.generated.model.PageEmployeeDto;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<PageEmployeeDto> getAllEmployees(@NotNull @Valid Integer pageNum, @NotNull @Valid Integer pageSize,Long excludeId) {
        return new ResponseEntity<>(employeeService.getAllEmployees(pageNum, pageSize,excludeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeVacations(@NotNull @Valid Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateVacationsByEmployeeId(@Valid EmployeeDto dto) {
        employeeService.updateEmployeeVacations(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> deleteEmployeeById(@NotNull @Valid Long id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteVacationById(@NotNull @Valid Long vacationId) {
        employeeService.deleteVacationById(vacationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users" + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Employee> listUsers = employeeService.findAllToExport();
        EmployeeVacationsToExcelConfig excelExporter = new EmployeeVacationsToExcelConfig(listUsers);
        excelExporter.export(response);
    }
}
