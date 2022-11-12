package ru.acuma.shuffler.service.season;

import net.steppschuh.markdowngenerator.table.Table;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.api.TextBuilderService;
import ru.acuma.shufflerlib.model.web.entity.WebPlayerDetails;

import java.util.List;

@Service
public class TextBuilderServiceImpl implements TextBuilderService {

    public static final String TABLE_EDGE = "```";

    @Override
    public String buildSeasonReport(List<WebPlayerDetails> players) {

        Table.Builder tableBuilder = new Table.Builder()
                .addRow("Игрок", "Рейтинг");
        players.forEach(player -> appendRow(player, tableBuilder));

        return "Результаты сезона:\n\n"
                + TABLE_EDGE + "\n"
                + tableBuilder.build()
                + TABLE_EDGE;
    }

    private void appendRow(WebPlayerDetails player, Table.Builder tableBuilder) {
        tableBuilder.addRow(player.getName(), player.getScore());
    }

}
