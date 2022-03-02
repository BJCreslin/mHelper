package ru.mhelper.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.mhelper.repository.ProcurementRepo;
import ru.mhelper.services.parsers_dispatcher.Dispatcher;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.dto.ProcurementAddress;

import java.util.Optional;

@Service
public class ProcurementServiceImpl implements ProcurementService {
    private final Dispatcher dispatcher;
    private final ProcurementRepo repository;

    public ProcurementServiceImpl(Dispatcher dispatcher, ProcurementRepo repository) {
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


}
