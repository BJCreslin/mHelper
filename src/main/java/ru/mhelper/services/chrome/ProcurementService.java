package ru.mhelper.services.chrome;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.models.users.User;

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
