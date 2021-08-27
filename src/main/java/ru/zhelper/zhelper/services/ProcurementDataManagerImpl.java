package ru.zhelper.zhelper.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.exceptions.DataManagerException;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImpl.class);
	
    @Autowired
	private ProcurementRepo repository;

	@Override
	public Procurement loadEntity(Long idToLoad) {
		if (idToLoad == null) {
			throw new DataManagerException(
					DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA, null);
		}
		
		Procurement procurement = null;
		try {
			procurement = repository.getById(idToLoad);
		} catch (RuntimeException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToLoad);
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
		return procurement;
	}

	@Override
	public Procurement saveEntity(Procurement procurement) {
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
	public void deleteEntity(Procurement procurement) {
		if (procurement == null) {
			throw new DataManagerException(
					DataManagerException.COULD_NOT_DELETE_PROCUREMENT_NULL_DATA, null);
		}
		try {
			repository.delete(procurement);
		} catch (RuntimeException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, procurement.getId());
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
	}

	@Override
	public void deleteEntityById(Long idToDelete) {
		try {
			repository.deleteById(idToDelete);
		} catch (RuntimeException e) {
			String msg = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToDelete);
			LOGGER.warn(msg);
			throw new DataManagerException(msg, e);
		}
	}
	
	@Override
	public List<Procurement> getProcurementsByFzNumber(Integer fzNumber) {
		if (fzNumber == null) {
			return Collections.emptyList();
		}
		return repository.findAll().stream().filter(proc -> fzNumber.equals(proc.getFzNumber()))
				.collect(Collectors.toList());
	}
}
