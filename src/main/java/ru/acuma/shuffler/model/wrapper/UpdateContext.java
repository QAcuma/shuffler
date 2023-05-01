package ru.acuma.shuffler.model.wrapper;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.domain.TEventPlayer;

@Data
@Builder
@Accessors(chain = true)
public class UpdateContext {

    private Message message;

    private TEventPlayer eventPlayer;

}
