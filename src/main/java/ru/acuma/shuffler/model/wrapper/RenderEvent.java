package ru.acuma.shuffler.model.wrapper;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.domain.TRender;

@Data
@Builder
@Accessors(chain = true)
public class RenderEvent {

    private Long chatId;

    private TRender render;

}
