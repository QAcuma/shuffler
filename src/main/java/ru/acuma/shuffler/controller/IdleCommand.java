package ru.acuma.shuffler.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.constant.Command;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdleCommand implements BaseBotCommand {

    @Override
    public void execute(final Message message, final String... args) {
        log.debug("Idle command");
    }

    @Override
    public String getCommandIdentifier() {
        return Command.IDLE.getName();
    }
}

