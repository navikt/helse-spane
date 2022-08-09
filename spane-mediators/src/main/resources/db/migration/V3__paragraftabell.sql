CREATE TABLE paragrafsøk
(
    id       SERIAL,
    paragraf VARCHAR(32) NOT NULL,
    ledd     int,
    bokstav  char,
    punktum  int,
    PRIMARY KEY (paragraf, ledd, bokstav, punktum),
    UNIQUE(id)

);

CREATE TABLE person_paragrafsøk
(
    fnr VARCHAR(32) references person (fnr) on update cascade on delete cascade not null ,
    id SERIAL references paragrafsøk (id) on update cascade on delete cascade not null ,
    PRIMARY KEY (fnr, id)
);


