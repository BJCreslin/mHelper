package ru.thelper.services.dao;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.thelper.exceptions.UserException;
import ru.thelper.models.BaseStatus;
import ru.thelper.models.procurements.Procurement;
import ru.thelper.models.user_procurement.UserProcurementLinks;
import ru.thelper.models.users.User;
import ru.thelper.repository.ProcurementRepository;
import ru.thelper.repository.UserProcurementLinksRepository;
import ru.thelper.services.exceptions.DataManagerException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcurementDataManagerImpl implements ProcurementDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImpl.class);

    public static final String CREATED_BEFORE = "PROCUREMENTS CREATED BEFORE {}";

    public static final String ALL_PROCUREMENTS_NUMBER = "Load all procurements. Number: {}.";

    private final ProcurementRepository repository;

    public ProcurementDataManagerImpl(ProcurementRepository repository, UserProcurementLinksRepository userProcurementLinksRepository) {
        this.repository = repository;
        this.userProcurementLinksRepository = userProcurementLinksRepository;
    }

    private final UserProcurementLinksRepository userProcurementLinksRepository;


    @Override
    public Procurement loadById(Long idToLoad) {
        if (idToLoad == null) {
            throw new DataManagerException(
                    DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA, null);
        }

        Procurement procurement;
        procurement = repository.findById(idToLoad).orElseThrow(() -> new DataManagerException(String.format(
                DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, idToLoad)));
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(">>>>>>>>>> PROCUREMENT LOADED BY ID {}: {}", idToLoad, procurement);
        }
        return procurement;
    }

    @Override
    public Page<Procurement> loadByIdList(List<Long> idsToLoad, Pageable pageable) {
        return repository.findByIdIn(idsToLoad, pageable);
    }

    @Override

    public Procurement save(Procurement procurement, User user) {
        checkProcurementAndUser(procurement, user);
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(">>>>>>>>>> SAVE PROCUREMENT: {}", procurement);
            }
            procurement = repository.save(procurement);
            userProcurementLinksRepository.save(createUserProcurementLink(user, procurement));
            return procurement;
        } catch (DataManagerException dataMgrExc) {
            LOGGER.error(DataManagerException.COULD_NOT_SAVE_PROCUREMENT);
            throw new DataManagerException(DataManagerException.COULD_NOT_SAVE_PROCUREMENT, dataMgrExc);
        }
    }

    private void checkProcurementAndUser(Procurement procurement, User user) {
        if (procurement == null) {
            throw new DataManagerException(
                    DataManagerException.COULD_NOT_SAVE_PROCUREMENT_NULL_DATA);
        }
        if (user == null) {
            throw new UserException(UserException.USER_NULL);
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
    public Page<Procurement> loadPageableCreatedBeforeDate(LocalDate date, Pageable pageable) {
        var result = repository.findByLessThanDate(date, pageable);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(CREATED_BEFORE, LocalDate.now());
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
        return repository.getByUin(uin);
    }

    @Override
    public Page<Procurement> loadAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Procurement> loadAll() {
        List<Procurement> result = repository.findAll();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(ALL_PROCUREMENTS_NUMBER, result.size());
        }
        return result;
    }

    private UserProcurementLinks createUserProcurementLink(User user, Procurement procurement) {
        return UserProcurementLinks.builder()
                .procurement(procurement)
                .user(user)
                .status(BaseStatus.ACTIVE).build();
    }
}
