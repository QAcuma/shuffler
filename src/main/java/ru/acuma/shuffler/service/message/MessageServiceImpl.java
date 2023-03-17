package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.enums.Constants;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.MessageService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final KeyboardService keyboardService;
    private final MessageContentService messageContentService;

    @Override
    public BotApiMethod<Message> buildMessage(TgEvent event, MessageType type) {
        return SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .text(messageContentService.buildContent(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public EditMessageText buildMessageUpdate(TgEvent event, Integer messageId, MessageType type) {
        return EditMessageText.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .text(messageContentService.buildContent(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public EditMessageReplyMarkup buildReplyMarkupUpdate(TgEvent event, Integer messageId, MessageType type) {
        return EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .replyMarkup(getKeyboard(event, type))
                .build();
    }

    @Override
    public EditMessageText buildLobbyMessageUpdate(TgEvent event) {
        return buildMessageUpdate(event, event.getBaseMessage(), MessageType.LOBBY);
    }

    @Override
    public PinChatMessage pinMessage(Message message) {
        return PinChatMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .messageId(message.getMessageId())
                .build();
    }

    @Override
    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .build();
    }

    private InlineKeyboardMarkup getKeyboard(TgEvent event, MessageType type) {
        return switch (type) {
            case LOBBY -> keyboardService.getLobbyKeyboard(event);
            case CHECKING -> keyboardService.getCheckingKeyboard(event);
            case GAME -> keyboardService.getGamingKeyboard(event);
            case CHECKING_TIMED -> keyboardService.getTimedKeyboard(Constants.DISABLED_BUtTON_TIMEOUT);
            case CANCELLED, STAT -> keyboardService.getEmptyKeyboard();
        };
    }
}
