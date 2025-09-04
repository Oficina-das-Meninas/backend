create table events (
                        id uuid primary key
    ,title varchar(500)
    ,preview_image_url varchar(4000)
    ,description varchar(500)
    ,amount numeric(15, 2)
    ,event_date TIMESTAMP WITHOUT TIME ZONE
    ,location varchar(200)
    ,url_to_platform varchar(4000)
);

alter table events alter column amount type numeric(15, 2)

insert into events
values (gen_random_uuid(),
        'Pokemon GO da Oficina',
        'pokemon-go.png',
        'Neste sábado 06/09 estaremos todos caçando pokemon na praça são roque! Venha conosco nessa jornada!',
        2563.90,
        '2025-09-06 13:00:00',
        'Praça São Roque/Araraquara',
        'https://seu-evento-online.com.br/oficina/pokemon-go');

select *
from events;