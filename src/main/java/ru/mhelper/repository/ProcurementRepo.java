package ru.mhelper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mhelper.models.procurements.Procurement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcurementRepo extends JpaRepository<Procurement, Long> {

    Procurement save(Procurement procurement);

    Optional<Procurement> getByUin(String uin);

    Page<Procurement> findByIdIn(List<Long> ids, Pageable pageable);

    @Query("select p from Procurement p where p.dateOfPlacement < :date")
    Page<Procurement> findByLessThanDate(LocalDate date, Pageable page);

}
