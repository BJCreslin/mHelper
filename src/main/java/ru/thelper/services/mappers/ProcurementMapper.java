package ru.thelper.services.mappers;

import org.mapstruct.Mapper;
import ru.thelper.models.dto.ProcurementDto;
import ru.thelper.models.procurements.Procurement;

@Mapper(componentModel = "spring")
public interface ProcurementMapper {

    Procurement dtoToEntity(ProcurementDto dto);

    ProcurementDto dboToDto(Procurement dbo);
}
