package ru.thelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.thelper.models.user_procurement.UserProcurementLinks;

public interface UserProcurementLinksRepository extends JpaRepository<UserProcurementLinks, Long> {
}
