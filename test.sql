create table matchday_schema.`match`
(
    first_half_end_time    time(6) null,
    first_half_start_time  time(6) null,
    match_date             date                            not null,
    planned_end_time       time(6)                         not null,
    planned_start_time     time(6)                         not null,
    second_half_end_time   time(6) null,
    second_half_start_time time(6) null,
    away_team_id           bigint                          not null,
    home_team_id           bigint                          not null,
    id                     bigint auto_increment
        primary key,
    stadium                varchar(50)                     not null,
    assistant_referee_1    varchar(255) null,
    assistant_referee_2    varchar(255) null,
    fourth_referee         varchar(255) null,
    main_referee           varchar(255) null,
    memo                   varchar(255) null,
    title                  varchar(255)                    not null,
    match_state            varchar(50) default 'SCHEDULED' not null,
    match_type             enum ('대회', '리그', '친선경기')       not null,
    constraint FKbe18et1qmp9kl2b70n6cu6y5g
        foreign key (home_team_id) references matchday_schema.team (id),
    constraint FKct67nlwtvkl85iypgono6pnen
        foreign key (away_team_id) references matchday_schema.team (id)
) collate = utf8mb4_unicode_ci;

