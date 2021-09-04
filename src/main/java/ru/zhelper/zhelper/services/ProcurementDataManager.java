package ru.zhelper.zhelper.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ru.zhelper.zhelper.models.Procurement;

public interface ProcurementDataManager {
    
    Procurement loadById(Long id);
    
    Procurement save(Procurement procurement);
    
    void delete(Procurement procurement);
    
    void deleteById(Long idToDelete);

	List<Procurement> getListByFzNumber(Integer fzNumber);
	
	Page<Procurement> findAll();
	
	Pageable getPageableListByIdList(List<Integer> idList);
	
	Pageable getPageableListOfCreatedBeforeDate(LocalDate date);
}
