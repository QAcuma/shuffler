package ru.acuma.shuffler.service.season;

import net.steppschuh.markdowngenerator.table.Table;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.UserInfo;

import java.util.List;

@Service
public class TextBuilderService {

    public static final String TABLE_EDGE = "```";

    @Transactional(propagation = Propagation.MANDATORY)
    public String buildSeasonReport(final List<Rating> players) {

        Table.Builder tableBuilder = new Table.Builder()
            .addRow("Игрок", "Рейтинг");
        players.forEach(player -> appendRow(player, tableBuilder));

        return "Результаты сезона:\n\n"
            + TABLE_EDGE + "\n"
            + tableBuilder.build()
            + TABLE_EDGE;
    }

    private void appendRow(Rating rating, Table.Builder tableBuilder) {
        var name = getName(rating.getPlayer().getUser());
        tableBuilder.addRow(name, rating.getScore());
    }

    private String getName(final UserInfo user) {
        return user.getFirstName() +
            " " +
            getFormatName(user);
    }

    private String getFormatName(final UserInfo user) {
        String lastName = "";
        if (user.getLastName() != null) {
            lastName = user.getLastName().charAt(0) + ".";
        }
        StringUtils.capitalize(lastName);

        return lastName;
    }
}
