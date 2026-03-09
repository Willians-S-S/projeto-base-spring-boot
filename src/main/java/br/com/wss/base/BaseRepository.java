/**
 * Base repository class to be implemented by the repository layer.
 */
package br.com.wss.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Willians Silva Santos
 * @param <E>
 * @param <ID>
 *
 */
public interface BaseRepository<E extends BaseEntity<?>, ID extends Serializable>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {}