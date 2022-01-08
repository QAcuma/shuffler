update pg_database
set encoding = pg_char_to_encoding('UTF8')
where datname = 'ks_db';
update pg_database
set encoding = pg_char_to_encoding('UTF8')
where datname = 'ks_local_db';
