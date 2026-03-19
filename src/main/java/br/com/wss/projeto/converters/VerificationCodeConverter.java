package br.com.wss.projeto.converters;

import br.com.wss.base.BaseConverter;
import br.com.wss.projeto.dtos.VerificationCodeDTO;
import br.com.wss.projeto.entities.VerificationCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { AccountConverter.class })
public interface VerificationCodeConverter extends BaseConverter<VerificationCode, VerificationCodeDTO> {

    VerificationCodeConverter INSTANCE = Mappers.getMapper(VerificationCodeConverter.class);

    @Override
    VerificationCodeDTO convertToDTO(VerificationCode entity);

    @Override
    @Mapping(target = "account", ignore = true)
    VerificationCode convertToEntity(VerificationCodeDTO dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "account", ignore = true)
    VerificationCode convertToEntityInsert(VerificationCodeDTO dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "account", ignore = true)
    void updateEntityFromDTO(@MappingTarget VerificationCode entity, VerificationCodeDTO dto);
}
