package ru.mhelper.services.mappers;

import org.mapstruct.Mapper;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.models.procurements.Procurement;

@Mapper(componentModel = "spring")
public interface ProcurementMapper {

    Procurement dtoToEntity(ProcurementDto dto);

    ProcurementDto dboToDto(Procurement dbo);
}
