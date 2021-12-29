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
import org.jooq.Row6;
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
import ru.acuma.k.shuffler.tables.records.TeamRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Team extends TableImpl<TeamRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.team</code>
     */
    public static final Team TEAM = new Team();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TeamRecord> getRecordType() {
        return TeamRecord.class;
    }

    /**
     * The column <code>public.team.id</code>.
     */
    public final TableField<TeamRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.team.game_id</code>.
     */
    public final TableField<TeamRecord, Long> GAME_ID = createField(DSL.name("game_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.team.messages</code>.
     */
    public final TableField<TeamRecord, JSONB> MESSAGES = createField(DSL.name("messages"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.team.players</code>.
     */
    public final TableField<TeamRecord, JSONB> PLAYERS = createField(DSL.name("players"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.team.started_at</code>.
     */
    public final TableField<TeamRecord, OffsetDateTime> STARTED_AT = createField(DSL.name("started_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>public.team.finished_at</code>.
     */
    public final TableField<TeamRecord, OffsetDateTime> FINISHED_AT = createField(DSL.name("finished_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Team(Name alias, Table<TeamRecord> aliased) {
        this(alias, aliased, null);
    }

    private Team(Name alias, Table<TeamRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.team</code> table reference
     */
    public Team(String alias) {
        this(DSL.name(alias), TEAM);
    }

    /**
     * Create an aliased <code>public.team</code> table reference
     */
    public Team(Name alias) {
        this(alias, TEAM);
    }

    /**
     * Create a <code>public.team</code> table reference
     */
    public Team() {
        this(DSL.name("team"), null);
    }

    public <O extends Record> Team(Table<O> child, ForeignKey<O, TeamRecord> key) {
        super(child, key, TEAM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<TeamRecord, Long> getIdentity() {
        return (Identity<TeamRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TeamRecord> getPrimaryKey() {
        return Keys.TEAM_PKEY;
    }

    @Override
    public List<ForeignKey<TeamRecord, ?>> getReferences() {
        return Arrays.asList(Keys.TEAM__FK_EXISTS_GAME);
    }

    private transient Game _game;

    public Game game() {
        if (_game == null)
            _game = new Game(this, Keys.TEAM__FK_EXISTS_GAME);

        return _game;
    }

    @Override
    public Team as(String alias) {
        return new Team(DSL.name(alias), this);
    }

    @Override
    public Team as(Name alias) {
        return new Team(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Team rename(String name) {
        return new Team(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Team rename(Name name) {
        return new Team(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, JSONB, JSONB, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
