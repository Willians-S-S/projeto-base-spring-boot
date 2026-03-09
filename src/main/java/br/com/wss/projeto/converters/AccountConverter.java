package br.com.wss.projeto.converters;

import br.com.wss.base.BaseConverter;
import br.com.wss.projeto.dtos.AccountDTO;
import br.com.wss.projeto.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountConverter extends BaseConverter<Account, AccountDTO> {

    // Instância para uso fora do Spring
    AccountConverter INSTANCE = Mappers.getMapper(AccountConverter.class);

    @Override
    AccountDTO convertToDTO(Account entity);

    @Override
    Account convertToEntity(AccountDTO dto);

    @Mapping(target = "uid", ignore = true)
    Account convertToEntityInsert(AccountDTO dto);

    @Mapping(target = "uid", ignore = true)
    void updateEntityFromDTO(@MappingTarget Account entity, AccountDTO dto);
}