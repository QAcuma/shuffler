/*
 * This file is generated by jOOQ.
 */
package ru.acuma.k.shuffler.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import ru.acuma.k.shuffler.Keys;
import ru.acuma.k.shuffler.Public;
import ru.acuma.k.shuffler.tables.records.EventRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Event extends TableImpl<EventRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.event</code>
     */
    public static final Event EVENT = new Event();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EventRecord> getRecordType() {
        return EventRecord.class;
    }

    /**
     * The column <code>public.event.id</code>.
     */
    public final TableField<EventRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.event.chat_id</code>.
     */
    public final TableField<EventRecord, Long> CHAT_ID = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.event.messages</code>.
     */
    public final TableField<EventRecord, JSONB> MESSAGES = createField(DSL.name("messages"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.event.players</code>.
     */
    public final TableField<EventRecord, JSONB> PLAYERS = createField(DSL.name("players"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.event.status</code>.
     */
    public final TableField<EventRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.event.started_at</code>.
     */
    public final TableField<EventRecord, OffsetDateTime> STARTED_AT = createField(DSL.name("started_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>public.event.finished_at</code>.
     */
    public final TableField<EventRecord, OffsetDateTime> FINISHED_AT = createField(DSL.name("finished_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Event(Name alias, Table<EventRecord> aliased) {
        this(alias, aliased, null);
    }

    private Event(Name alias, Table<EventRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.event</code> table reference
     */
    public Event(String alias) {
        this(DSL.name(alias), EVENT);
    }

    /**
     * Create an aliased <code>public.event</code> table reference
     */
    public Event(Name alias) {
        this(alias, EVENT);
    }

    /**
     * Create a <code>public.event</code> table reference
     */
    public Event() {
        this(DSL.name("event"), null);
    }

    public <O extends Record> Event(Table<O> child, ForeignKey<O, EventRecord> key) {
        super(child, key, EVENT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<EventRecord, Long> getIdentity() {
        return (Identity<EventRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<EventRecord> getPrimaryKey() {
        return Keys.EVENT_PKEY;
    }

    @Override
    public List<ForeignKey<EventRecord, ?>> getReferences() {
        return Arrays.asList(Keys.EVENT__FK_EXISTS_CHAT);
    }

    private transient GroupInfo _groupInfo;

    public GroupInfo groupInfo() {
        if (_groupInfo == null)
            _groupInfo = new GroupInfo(this, Keys.EVENT__FK_EXISTS_CHAT);

        return _groupInfo;
    }

    @Override
    public Event as(String alias) {
        return new Event(DSL.name(alias), this);
    }

    @Override
    public Event as(Name alias) {
        return new Event(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Event rename(String name) {
        return new Event(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Event rename(Name name) {
        return new Event(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, JSONB, JSONB, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
