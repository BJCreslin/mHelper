package ru.zhelper.zhelper.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.procurements.Procurement;
import ru.zhelper.zhelper.models.dto.ProcurementAddress;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.parsers_dispatcher.Dispatcher;

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
