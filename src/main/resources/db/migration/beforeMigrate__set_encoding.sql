update pg_database
set encoding = pg_char_to_encoding('UTF8')
where pg_database.datname like 'shuffler%';
