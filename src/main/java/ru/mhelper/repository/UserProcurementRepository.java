package ru.mhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mhelper.models.user_procurement.UserProcurementLinks;

public interface UserProcurementRepository extends JpaRepository<UserProcurementLinks, Long> {
}
