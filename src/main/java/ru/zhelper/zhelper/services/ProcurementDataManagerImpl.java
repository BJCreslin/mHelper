package ru.zhelper.zhelper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {

    @Autowired
	private ProcurementRepo repository;

	@Override
	public Procurement loadProcurement(Long id) {
		return repository.getById(id);
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
}
