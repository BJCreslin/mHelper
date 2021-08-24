package ru.zhelper.zhelper.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImpl.class);

    @Autowired
	private ProcurementRepo repository;

	@Override
	public Procurement loadProcurement(Long id) {
		Procurement p = null;
		
		try {
			p = repository.getById(id);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Found no Procurement with id {}!", id);
		}
		return p;
	}

	@Override
	public Procurement saveProcurement(Procurement p) {
		return repository.save(p);
	}

	@Override
	public void deleteProcurement(Procurement p) {
		repository.delete(p);
	}

	@Override
	public void deleteProcurementById(Long idToDelete) {
		repository.deleteById(idToDelete);
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
