package bgdn.vrbv.prjct.repository;

import bgdn.vrbv.prjct.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    Boolean existsByUsername(String username);

    @EntityGraph(value = "Employee.vacations")
    Page<Employee> findAllByIdNotNullAndIdNot(Pageable pageable,Long exclude);

}
