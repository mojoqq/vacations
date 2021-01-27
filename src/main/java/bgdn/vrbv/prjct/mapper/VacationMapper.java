package bgdn.vrbv.prjct.mapper;

import bgdn.vrbv.prjct.entity.Vacation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import rbgdn.vrbv.prjct.controller.generated.model.VacationDto;

@Component
public class VacationMapper extends ModelMapper {

    public VacationDto convertToDto(Vacation entity) {
        return map(entity, VacationDto.class);
    }

    public Vacation convertToEntity(VacationDto dto) {
        return map(dto, Vacation.class);
    }
}
