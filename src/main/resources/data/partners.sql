create table partners (
                          id uuid primary key
    ,preview_url varchar(4000)
    ,name varchar(500)
);

insert into partners
values (gen_random_uuid(), 'logo-prefeitura-araraquara.png', 'Prefeitura de Araraquara');

select *
from partners;