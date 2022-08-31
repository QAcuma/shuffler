package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.tables.pojos.GroupInfo;
import ru.acuma.shufflerlib.model.Discipline;

public interface BroadcastService {

    void seasonResultBroadcast(Long seasonId);

    void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo);

    void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo, Discipline discipline);
}
