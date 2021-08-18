package ru.zhelper.zhelper.services;

import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.dto.ProcurementAddress;
import ru.zhelper.zhelper.repository.ProcurementRepo;

@Service
public class ProcurementServiceImpl implements ProcurementService {
    private final ZakupkiParser parser;
    private final ProcurementRepo repository;

    public ProcurementServiceImpl(ZakupkiParser parser, ProcurementRepo repository) {
        this.parser = parser;
        this.repository = repository;
    }


    @Override
    public void action(ProcurementAddress procurementAddress) {
        Procurement procurement = parser.parse(procurementAddress.getAddress());
        repository.save(procurement);
    }
}
