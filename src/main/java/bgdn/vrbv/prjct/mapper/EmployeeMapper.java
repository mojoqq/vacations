package bgdn.vrbv.prjct.mapper;

import bgdn.vrbv.prjct.entity.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import rbgdn.vrbv.prjct.controller.generated.model.EmployeeDto;
@Component
public class EmployeeMapper extends ModelMapper {

    public EmployeeDto convertToDto(Employee emoloyee){
        return map(emoloyee,EmployeeDto.class);
    }
}
