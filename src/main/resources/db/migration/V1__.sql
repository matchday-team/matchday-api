create table IF NOT EXISTS matchday_schema.team
(
    created_at     datetime(6)  not null,
    id             bigint auto_increment
        primary key,
    name           varchar(50)  not null,
    bottom_color   varchar(255) not null,
    stocking_color varchar(255) not null,
    team_color     varchar(255) not null,
    team_img       varchar(512) null,
    constraint UKg2l9qqsoeuynt4r5ofdt1x2td
        unique (name)
);

create table IF NOT EXISTS matchday_schema.`match`
(
    id                     bigint auto_increment
        primary key,
    first_half_start_time  time                            null,
    first_half_end_time    time(6)                         null,
    match_date             date                            not null,
    second_half_start_time time                            null,
    second_half_end_time   time(6)                         null,
    planned_start_time     time(6)                         not null,
    planned_end_time       time                            not null,
    away_team_id           bigint                          not null,
    home_team_id           bigint                          not null,
    stadium                varchar(50)                     not null,
    assistant_referee_1    varchar(255)                    null,
    assistant_referee_2    varchar(255)                    null,
    fourth_referee         varchar(255)                    null,
    main_referee           varchar(255)                    null,
    memo                   varchar(255)                    null,
    title                  varchar(255)                    not null,
    match_state            varchar(50) default 'SCHEDULED' not null,
    match_type             enum ('대회', '리그', '친선경기')       not null,
    first_half_period      int                             null,
    second_half_period     int                             null,
    constraint FKbe18et1qmp9kl2b70n6cu6y5g
        foreign key (home_team_id) references matchday_schema.team (id),
    constraint FKct67nlwtvkl85iypgono6pnen
        foreign key (away_team_id) references matchday_schema.team (id)
);

create fulltext index name_fulltext
    on matchday_schema.team (name);

create table IF NOT EXISTS matchday_schema.user
(
    id          bigint auto_increment
        primary key,
    name        varchar(30)  not null,
    profile_img varchar(512) null
);

create table IF NOT EXISTS matchday_schema.match_user
(
    id             bigint auto_increment
        primary key,
    match_id       bigint                                                   not null,
    team_id        bigint                                                   null,
    user_id        bigint                                                   null,
    match_position varchar(255)                                             null,
    role           enum ('ADMIN', 'ARCHIVES', 'START_PLAYER', 'SUB_PLAYER') not null,
    match_grid     int                                                      null,
    constraint FK1i23dd8n217ofmwv1pk9iw944
        foreign key (match_id) references matchday_schema.`match` (id),
    constraint FK4fky4xtx92uo3no5sq2a98rmv
        foreign key (team_id) references matchday_schema.team (id),
    constraint FKddbbpg2toa0rd749u9y5sk44x
        foreign key (user_id) references matchday_schema.user (id),
    check ((`match_grid` >= 0) and (`match_grid` <= 29))
);

create table IF NOT EXISTS matchday_schema.match_event
(
    event_time    datetime(6)                                                                                                                                            not null,
    id            bigint auto_increment
        primary key,
    match_id      bigint                                                                                                                                                 not null,
    match_user_id bigint                                                                                                                                                 not null,
    description   varchar(400)                                                                                                                                           null,
    event_type    enum ('ASSIST', 'CORNER_KICK', 'FOUL', 'GOAL', 'OFFSIDE', 'OWN_GOAL', 'RED_CARD', 'SHOT', 'SUB_IN', 'SUB_OUT', 'VALID_SHOT', 'WARNING', 'YELLOW_CARD') not null,
    constraint FK8u4y6u58lsasr2l7g64nvamp7
        foreign key (match_id) references matchday_schema.`match` (id),
    constraint FKbrebuu9hbw8xaeowfvqp2ghcs
        foreign key (match_user_id) references matchday_schema.match_user (id)
);

create table IF NOT EXISTS matchday_schema.user_team
(
    is_active        bit          null,
    number           int          null,
    id               bigint auto_increment
        primary key,
    join_date        datetime(6)  null,
    leave_date       datetime(6)  null,
    team_id          bigint       not null,
    user_id          bigint       not null,
    default_position varchar(255) null,
    constraint FK6d6agqknw564xtsa91d3259wu
        foreign key (team_id) references matchday_schema.team (id),
    constraint FKd6um0sk8hyytfq7oalt5a4nph
        foreign key (user_id) references matchday_schema.user (id)
);

