package ru.mhelper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.users.User;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {

    Optional<Procurement> getByUin(String uin);

    Page<Procurement> findByIdIn(List<Long> ids, Pageable pageable);

    @Query("select p from Procurement p where p.dateOfPlacement < :date")
    Page<Procurement> findByLessThanDate(LocalDate date, Pageable page);

    @Query("SELECT p FROM Procurement p WHERE p.status = ru.mhelper.models.BaseStatus.ACTIVE AND  p.applicationDeadline > :prevTime AND p.applicationDeadline < :nextTimeEvent")
    List<Procurement> getAllInTimeInterval(ZonedDateTime prevTime, ZonedDateTime nextTimeEvent);

//    Page<Procurement> getAllByUsersIs(Set<User> users, Pageable pageable);

    Page<Procurement> getAllByUsersIsIn(Set<User> users, Pageable pageable);
}
