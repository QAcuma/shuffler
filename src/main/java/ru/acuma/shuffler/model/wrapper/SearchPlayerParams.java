package ru.acuma.shuffler.model.wrapper;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class SearchPlayerParams {

    private Long userId;
    private Long chatId;
}
