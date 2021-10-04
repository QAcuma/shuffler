create table if not exists event_log
(
    id                         bigint generated always as identity primary key,
    user_id                    bigint,
    text                       text,
    audio_id bigint,
    document_id bigint,
    photo_id bigint,
    sticker_id bigint,
    video_id bigint,
    contact_id bigint,
    location jsonb,
    contact_id bigint,
    pinned_message_id bigint,
    left_chat_member_id bigint,
    new_chat_member_id bigint,
    new_chat_title text,
    new_chat_photo_id bigint,
    delete_chat_photo_id bigint,
    created_at                 timestamptz not null,
    updated_at                 timestamptz,
    deleted_at                 timestamptz
);
