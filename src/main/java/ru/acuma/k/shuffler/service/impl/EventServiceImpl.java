package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.service.EventService;
import ru.acuma.k.shuffler.service.MaintenanceService;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final MaintenanceService maintenanceService;


}
