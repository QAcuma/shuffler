insert into season (started_at, finished_at, name)
select now(), null, 'First season'
where not exists(select 1 from season where finished_at is null);