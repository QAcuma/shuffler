package ru.acuma.shuffler.service.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.tables.pojos.Season;
import ru.acuma.shufflerlib.repository.SeasonRepository;

import java.util.List;

@Service
@ConditionalOnSingleCandidate(SeasonRepository.class)
public class SeasonRepositoryMock implements SeasonRepository {
    @Override
    public Season getCurrentSeason() {
        return null;
    }

    @Override
    public void startNewSeason(String name) {

    }

    @Override
    public List<Season> findSeasons() {
        return null;
    }
}
