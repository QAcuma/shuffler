package ru.acuma.shuffler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;

@Slf4j
@Component
public class WaitCommand extends BaseBotCommand {

    public WaitCommand() {
        super(Command.WAIT.getCommand(), "Нужно подождать");
    }

    @Override
    public void execute(Message message) {
        log.debug("Нужно подождать таймер");
    }

}

