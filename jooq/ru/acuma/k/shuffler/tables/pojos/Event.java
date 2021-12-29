/*
 * This file is generated by jOOQ.
 */
package ru.acuma.k.shuffler.tables.pojos;


import java.io.Serializable;
import java.time.OffsetDateTime;

import org.jooq.JSONB;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long           id;
    private Long           chatId;
    private JSONB          messages;
    private JSONB          players;
    private String         status;
    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;

    public Event() {}

    public Event(Event value) {
        this.id = value.id;
        this.chatId = value.chatId;
        this.messages = value.messages;
        this.players = value.players;
        this.status = value.status;
        this.startedAt = value.startedAt;
        this.finishedAt = value.finishedAt;
    }

    public Event(
        Long           id,
        Long           chatId,
        JSONB          messages,
        JSONB          players,
        String         status,
        OffsetDateTime startedAt,
        OffsetDateTime finishedAt
    ) {
        this.id = id;
        this.chatId = chatId;
        this.messages = messages;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    /**
     * Getter for <code>public.event.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.event.id</code>.
     */
    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>public.event.chat_id</code>.
     */
    public Long getChatId() {
        return this.chatId;
    }

    /**
     * Setter for <code>public.event.chat_id</code>.
     */
    public Event setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    /**
     * Getter for <code>public.event.messages</code>.
     */
    public JSONB getMessages() {
        return this.messages;
    }

    /**
     * Setter for <code>public.event.messages</code>.
     */
    public Event setMessages(JSONB messages) {
        this.messages = messages;
        return this;
    }

    /**
     * Getter for <code>public.event.players</code>.
     */
    public JSONB getPlayers() {
        return this.players;
    }

    /**
     * Setter for <code>public.event.players</code>.
     */
    public Event setPlayers(JSONB players) {
        this.players = players;
        return this;
    }

    /**
     * Getter for <code>public.event.status</code>.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>public.event.status</code>.
     */
    public Event setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>public.event.started_at</code>.
     */
    public OffsetDateTime getStartedAt() {
        return this.startedAt;
    }

    /**
     * Setter for <code>public.event.started_at</code>.
     */
    public Event setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    /**
     * Getter for <code>public.event.finished_at</code>.
     */
    public OffsetDateTime getFinishedAt() {
        return this.finishedAt;
    }

    /**
     * Setter for <code>public.event.finished_at</code>.
     */
    public Event setFinishedAt(OffsetDateTime finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Event (");

        sb.append(id);
        sb.append(", ").append(chatId);
        sb.append(", ").append(messages);
        sb.append(", ").append(players);
        sb.append(", ").append(status);
        sb.append(", ").append(startedAt);
        sb.append(", ").append(finishedAt);

        sb.append(")");
        return sb.toString();
    }
}
