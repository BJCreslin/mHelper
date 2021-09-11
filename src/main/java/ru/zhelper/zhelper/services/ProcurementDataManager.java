package ru.zhelper.zhelper.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.zhelper.zhelper.models.Procurement;

public interface ProcurementDataManager {
    
    Procurement loadById(Long id);
    
    Procurement save(Procurement procurement);
    
    void delete(Procurement procurement);
    
    void deleteById(Long idToDelete);

	List<Procurement> getListByFzNumber(Integer fzNumber);
	
	Page<Procurement> findAll(Pageable pageable);

	Page<Procurement> loadByIdList(List<Long> idsToLoad, Pageable pageable);

	/**
	 * https://stackoverflow.com/questions/45430202/spring-jpa-method-to-find-entities-with-beforeandequal-a-date-and-afterandequal/45430556
	 */
	Page<Procurement> loadCreatedBeforeDate(LocalDate date, Pageable pageable);
}
