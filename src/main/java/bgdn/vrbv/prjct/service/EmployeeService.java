package bgdn.vrbv.prjct.service;

import bgdn.vrbv.prjct.entity.Employee;
import bgdn.vrbv.prjct.entity.Vacation;
import bgdn.vrbv.prjct.mapper.EmployeeMapper;
import bgdn.vrbv.prjct.mapper.VacationMapper;
import bgdn.vrbv.prjct.repository.EmployeeRepository;
import bgdn.vrbv.prjct.repository.RoleRepository;
import bgdn.vrbv.prjct.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rbgdn.vrbv.prjct.controller.generated.model.EmployeeDto;
import rbgdn.vrbv.prjct.controller.generated.model.PageEmployeeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@EnableScheduling
@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final VacationMapper vacationMapper;
    private final VacationRepository vacationRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper, VacationMapper vacationMapper, VacationRepository vacationRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.vacationMapper = vacationMapper;
        this.vacationRepository = vacationRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByUsername(username);
        return employee.orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public List<Employee> findAllToExport(){
        return employeeRepository.findAll();
    }

    public PageEmployeeDto getAllEmployees(Integer pageNum, Integer pageSize,Long excludeId){
        Pageable pageable = PageRequest.of(pageNum == null || pageNum < 0
                ? 0 : pageNum, pageSize == null || pageSize < 1 ? 10 : pageSize, Sort.Direction.ASC,
                "lastname");
        PageEmployeeDto pageEmployeeDto = new PageEmployeeDto();
        Page<Employee> page = employeeRepository.findAllByIdNotNullAndIdNot(pageable,excludeId);
        pageEmployeeDto.setContent(page
                .getContent()
                .stream()
                .map(employeeMapper::convertToDto)
                .collect(Collectors.toList()));
        pageEmployeeDto.setNumber(page.getNumber());
        pageEmployeeDto.setSize(page.getSize());
        pageEmployeeDto.setTotalPages(page.getTotalPages());
        pageEmployeeDto.setTotalElements(page.getTotalElements());
        return pageEmployeeDto;
    }

    public EmployeeDto getEmployeeById(Long id){
        Optional<Employee> byId = employeeRepository.findById(id);
        if(byId.isPresent()){
            return employeeMapper.convertToDto(byId.get());
        };
        return null;
    }

    public void updateEmployeeVacations(EmployeeDto dto){
        Optional<Employee> byId = employeeRepository.findById(dto.getId());
        if(byId.isPresent()){
            List<Vacation> collect = dto.getVacations().stream().map(vacationMapper::convertToEntity)
                    .collect(Collectors.toList());
            Employee employee = byId.get();
            if(collect.size() > 0) {
                collect.forEach(vacation -> vacation.setEmployee(employee));
                employee.setVacations(collect);
            } else {
                employee.setVacations(new ArrayList<>());
            }
            employeeRepository.save(employee);
        }
    }

    public void deleteEmployeeById(Long id){
        employeeRepository.deleteById(id);
    }

    public void deleteVacationById(Long id){
        vacationRepository.deleteById(id);
    }

}