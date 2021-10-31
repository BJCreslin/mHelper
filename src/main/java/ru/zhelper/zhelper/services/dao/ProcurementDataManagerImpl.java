package ru.zhelper.zhelper.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.exceptions.DataManagerException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImpl.class);

    private ProcurementRepo repository;

    @Autowired
    public void setRepository(ProcurementRepo repo) {
        this.repository = repo;
    }

    @Override
    public Procurement loadById(Long idToLoad) {
        if (idToLoad == null) {
            throw new DataManagerException(
                    DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA, null);
        }

        Procurement procurement;
        try {
            procurement = repository.getById(idToLoad);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(">>>>>>>>>> PROCUREMENT LOADED BY ID {}: {}", idToLoad, procurement);
            }
        } catch (EntityNotFoundException e) {
            String msg = String.format(
                    DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToLoad);
            LOGGER.error(msg);
            throw new DataManagerException(msg, e);
        }
        return procurement;
    }

    @Override
    public Page<Procurement> loadByIdList(List<Long> idsToLoad, Pageable pageable) {
        return repository.findByIdIn(idsToLoad, pageable);
    }

    @Override
    public Procurement save(Procurement procurement) {
        if (procurement == null) {
            throw new DataManagerException(
                    DataManagerException.COULD_NOT_SAVE_PROCUREMENT_NULL_DATA, null);
        }
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(">>>>>>>>>> SAVE PROCUREMENT: {}", procurement);
            }
            return repository.save(procurement);
        } catch (DataManagerException dataMgrExc) {
            LOGGER.error(DataManagerException.COULD_NOT_SAVE_PROCUREMENT);
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
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(">>>>>>>>>> DELETE PROCUREMENT: {}", procurement);
            }
            repository.delete(procurement);
        } catch (EntityNotFoundException e) {
            String msg = String.format(
                    DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, procurement.getId());
            LOGGER.error(msg);
            throw new DataManagerException(msg, e);
        }
    }

    @Override
    public void deleteById(Long idToDelete) {
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(">>>>>>>>>> DELETE PROCUREMENT BY ID: {}", idToDelete);
            }
            repository.deleteById(idToDelete);
        } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
            String msg = String.format(
                    DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToDelete);
            LOGGER.error(msg);
            throw new DataManagerException(msg, e);
        }
    }

    @Override
    public List<Procurement> loadListByFzNumber(Integer fzNumber) {
        if (fzNumber == null) {
            return Collections.emptyList();
        }
        var filtered = repository.findAll().stream().filter(proc -> fzNumber.equals(proc.getFzNumber()))
                .collect(Collectors.toList());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" >>>>>>>>>> PROCUREMENTS WITH fzNumber {}:", fzNumber);
        }
        filtered.forEach(procurement -> LOGGER.info(procurement.toString()));
        return filtered;
    }

    @Override
    public Page<Procurement> loadCreatedBeforeDate(LocalDate date, Pageable pageable) {
        var result = repository.findByLessThanDate(date, pageable);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" >>>>>>>>>> PROCUREMENTS CREATED BEFORE 01.02.2021:");
        }
        result.stream().forEach(procurement -> LOGGER.info(procurement.toString()));
        return result;
    }

    @Override
    public Optional<Procurement> loadByUin(String uin) {
        if (uin == null || uin.isEmpty() || uin.isBlank()) {
            LOGGER.error(DataManagerException.UIN_IS_EMPTY);
            throw new DataManagerException(DataManagerException.UIN_IS_EMPTY);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Find PROCUREMENT by UIN: {}", uin);
        }
        return Optional.of(repository.getByUin(uin));
    }

    @Override
    public Page<Procurement> loadAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
