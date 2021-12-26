/*
 * This file is generated by jOOQ.
 */
package ru.acuma.k.shuffler.tables.records;


import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import ru.acuma.k.shuffler.tables.GroupInfo;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GroupInfoRecord extends UpdatableRecordImpl<GroupInfoRecord> implements Record4<Long, Long, String, JSONB> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.group_info.id</code>.
     */
    public GroupInfoRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.group_info.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.group_info.telegram_id</code>.
     */
    public GroupInfoRecord setTelegramId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.group_info.telegram_id</code>.
     */
    public Long getTelegramId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.group_info.title</code>.
     */
    public GroupInfoRecord setTitle(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.group_info.title</code>.
     */
    public String getTitle() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.group_info.members</code>.
     */
    public GroupInfoRecord setMembers(JSONB value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.group_info.members</code>.
     */
    public JSONB getMembers() {
        return (JSONB) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Long, String, JSONB> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, Long, String, JSONB> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return GroupInfo.GROUP_INFO.ID;
    }

    @Override
    public Field<Long> field2() {
        return GroupInfo.GROUP_INFO.TELEGRAM_ID;
    }

    @Override
    public Field<String> field3() {
        return GroupInfo.GROUP_INFO.TITLE;
    }

    @Override
    public Field<JSONB> field4() {
        return GroupInfo.GROUP_INFO.MEMBERS;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getTelegramId();
    }

    @Override
    public String component3() {
        return getTitle();
    }

    @Override
    public JSONB component4() {
        return getMembers();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getTelegramId();
    }

    @Override
    public String value3() {
        return getTitle();
    }

    @Override
    public JSONB value4() {
        return getMembers();
    }

    @Override
    public GroupInfoRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public GroupInfoRecord value2(Long value) {
        setTelegramId(value);
        return this;
    }

    @Override
    public GroupInfoRecord value3(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public GroupInfoRecord value4(JSONB value) {
        setMembers(value);
        return this;
    }

    @Override
    public GroupInfoRecord values(Long value1, Long value2, String value3, JSONB value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GroupInfoRecord
     */
    public GroupInfoRecord() {
        super(GroupInfo.GROUP_INFO);
    }

    /**
     * Create a detached, initialised GroupInfoRecord
     */
    public GroupInfoRecord(Long id, Long telegramId, String title, JSONB members) {
        super(GroupInfo.GROUP_INFO);

        setId(id);
        setTelegramId(telegramId);
        setTitle(title);
        setMembers(members);
    }

    /**
     * Create a detached, initialised GroupInfoRecord
     */
    public GroupInfoRecord(ru.acuma.k.shuffler.tables.pojos.GroupInfo value) {
        super(GroupInfo.GROUP_INFO);

        if (value != null) {
            setId(value.getId());
            setTelegramId(value.getTelegramId());
            setTitle(value.getTitle());
            setMembers(value.getMembers());
        }
    }
}