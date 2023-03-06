package it.bitrock.bitrockairways.model.mapper;

import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.model.dto.PlaneCreateDTO;
import it.bitrock.bitrockairways.model.dto.PlaneUpdateDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface PlaneMapper {
    PlaneMapper INSTANCE = Mappers.getMapper(PlaneMapper.class);


    Plane planeCreateDtoToPlane(PlaneCreateDTO planeCreateDTO);

    Plane planeUpdateDtoToPlane(PlaneUpdateDTO planeUpdateDTO);
}
