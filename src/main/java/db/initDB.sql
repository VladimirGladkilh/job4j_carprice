CREATE TABLE public.car
(
    id          SERIAL PRIMARY KEY,
    body        character varying(255),
    created     timestamp without time zone,
    description character varying(255),
    enginetype  character varying(255),
    gear        character varying(255),
    price       double precision NOT NULL default 0,
    privod      character varying(255),
    probeg      integer          NOT NULL default 0,
    saled       boolean          NOT NULL default 0,
    year        integer          NOT NULL default 0,
    marka_id    integer,
    model_id    integer,
    user_id     integer
);

CREATE TABLE public.marka
(
    id   SERIAL PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE public.model
(
    id       SERIAL PRIMARY KEY,
    name     character varying(255),
    marka_id integer
);

CREATE TABLE public.photo
(
    id     SERIAL PRIMARY KEY,
    path   character varying(255),
    car_id integer
);

CREATE TABLE public.users
(
    id       SERIAL PRIMARY KEY,
    email    character varying(255),
    name     character varying(255),
    password character varying(255)
);