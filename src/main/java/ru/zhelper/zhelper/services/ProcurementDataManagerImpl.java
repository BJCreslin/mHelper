package ru.zhelper.zhelper.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.exceptions.DataManagerException;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImpl.class);

	Pageable firstPageWithFiveElements = PageRequest.of(0, 5);

	Pageable secondPageWithFiveElements = PageRequest.of(1, 5);
	
	private ProcurementRepo repository;
    
	@Autowired
    public void setRepository(ProcurementRepo repo){
       this.repository = repo;
    }

	@Override
	public Procurement loadById(Long idToLoad) {
		if (idToLoad == null) {
			throw new DataManagerException(
					DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA, null);
		}
		
		Procurement procurement = null;
		try {
			procurement = repository.getById(idToLoad);
		} catch (EntityNotFoundException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToLoad);
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
		return procurement;
	}

	@Override
	public Procurement save(Procurement procurement) {
		if (procurement == null) {
			throw new DataManagerException(
					DataManagerException.COULD_NOT_SAVE_PROCUREMENT_NULL_DATA, null);
		}

		try {
			return repository.save(procurement);
		} catch (DataManagerException dataMgrExc) {
			LOGGER.warn(DataManagerException.COULD_NOT_SAVE_PROCUREMENT);
			throw new DataManagerException(DataManagerException.COULD_NOT_SAVE_PROCUREMENT, dataMgrExc);
		}
	}

	@Override
	public void delete(Procurement procurement) {
		if (procurement == null) {
			throw new DataManagerException(
					DataManagerException.COULD_NOT_DELETE_PROCUREMENT_NULL_DATA, null);
		}
		try {
			repository.delete(procurement);
		} catch (EntityNotFoundException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, procurement.getId());
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
	}

	@Override
	public void deleteById(Long idToDelete) {
		try {
			repository.deleteById(idToDelete);
		} catch (EntityNotFoundException | EmptyResultDataAccessException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToDelete);
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
	}
	
	@Override
	public List<Procurement> getListByFzNumber(Integer fzNumber) {
		if (fzNumber == null) {
			return Collections.emptyList();
		}
		return repository.findAll().stream().filter(proc -> fzNumber.equals(proc.getFzNumber()))
				.collect(Collectors.toList());
	}

	@Override
	public Pageable getPageableListByIdList(List<Integer> idList) {
		// 5 = page size, 20 = page number
		Pageable pageable = PageRequest.of(5, 20);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable getPageableListOfCreatedBeforeDate(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Procurement> findAll() {
		Page<Procurement> allProcurementsSortedByName = repository.findAll(firstPageWithFiveElements);
		return allProcurementsSortedByName;
	}
}
