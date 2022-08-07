package ru.mhelper.services.procurement;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.repository.ProcurementRepository;
import ru.mhelper.services.parsers_dispatcher.Dispatcher;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProcurementServiceImpl implements ProcurementService {
    private final Dispatcher dispatcher;

    private final ProcurementRepository repository;

    public ProcurementServiceImpl(Dispatcher dispatcher, ProcurementRepository repository) {
        this.dispatcher = dispatcher;
        this.repository = repository;
    }

    @Override
    public void action(ProcurementAddress procurementAddress) {
        Procurement procurement = dispatcher.getFromUrl(procurementAddress.getAddress());
        Optional<Procurement> procurementFromDB = repository.getByUin(procurement.getUin());
        procurementFromDB.ifPresent(value -> BeanUtils.copyProperties(value, procurement, "id", "uin"));
        repository.save(procurement);
    }

    @Override
    public List<Procurement> getAllBeforeTime(LocalDateTime nextTimeEvent) {
        if (nextTimeEvent != null) {
            List<Procurement> procurementList = repository.getAllInTimeInterval(LocalDateTime.now(), nextTimeEvent);
            if (procurementList != null) {
                return procurementList;
            }
        }
        return Collections.emptyList();
    }
}
