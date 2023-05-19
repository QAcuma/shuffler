package ru.acuma.shuffler.model.constant.keyboards;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomButton implements KeyboardButton {

    private final String callback;
    private final String text;
    private final int row;
}
