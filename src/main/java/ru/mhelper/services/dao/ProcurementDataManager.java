package ru.mhelper.services.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.users.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProcurementDataManager {

    Procurement loadById(Long id);

    Procurement save(Procurement procurement, User user);

    void delete(Procurement procurement);

    void deleteById(Long idToDelete);

    List<Procurement> loadListByFzNumber(Integer fzNumber);

    Page<Procurement> loadAll(Pageable pageable);

    Page<Procurement> loadByIdList(List<Long> idsToLoad, Pageable pageable);

    Page<Procurement> loadPageableCreatedBeforeDate(LocalDate date, Pageable pageable);

    Optional<Procurement> loadByUin(String uin);

    List<Procurement> loadAll();
}
