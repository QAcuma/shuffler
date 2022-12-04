package ru.acuma.shuffler.service.season;

import ru.acuma.shufflerlib.model.web.entity.WebPlayerDetails;

import java.util.List;

public interface TextBuilderService {

    String buildSeasonReport(List<WebPlayerDetails> players);
}
