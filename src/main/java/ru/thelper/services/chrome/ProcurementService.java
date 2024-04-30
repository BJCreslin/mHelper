package ru.thelper.services.chrome;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.thelper.models.dto.ProcurementAddress;
import ru.thelper.models.dto.ProcurementDto;
import ru.thelper.models.users.User;

public interface ProcurementService {

    void save(ProcurementDto procurementDto);

    void save(UserDetails user, ProcurementDto procurementDto);

    void action(ProcurementAddress procurementAddress);

    /**
     * Получение списка закупок для пользователя user c пагинацией
     *
     * @param user     Пользователь
     * @param pageable Пагинация
     * @return Список закупок
     */
    Page<ProcurementDto> getProcurements(User user, Pageable pageable);
}
