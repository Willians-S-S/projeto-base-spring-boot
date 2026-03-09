package br.com.wss.base;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Interface base para converters da aplicação.
 * @author Willians Silva Santos
 */
public interface BaseConverter<E extends BaseEntity<?>, DTO> extends Serializable {

    /**
     * Converte uma entidade para DTO.
     */
    DTO convertToDTO(E entity);
    
    /**
     * Converte um DTO para entidade.
     */
    E convertToEntity(DTO dto);
    
    /**
     * Converte lista de entidades para DTOs, filtrando deletados.
     */
    default List<DTO> convertToDTOList(final List<E> entities) {
        if (entities == null) return Collections.emptyList();
        
        return entities.stream()
                .filter(this::isEntityValidForConversion)
                .map(this::convertToDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de DTOs para entidades.
     */
    default List<E> convertToEntityList(final List<DTO> dtos) {
        if (dtos == null) return Collections.emptyList();
        
        return dtos.stream()
                .map(this::convertToEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica se a entidade é válida para conversão.
     */
    default boolean isEntityValidForConversion(final BaseEntity<?> entity) {
        return entity != null && !Boolean.TRUE.equals(entity.getDeleted());
    }
}