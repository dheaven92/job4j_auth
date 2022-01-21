create table employee (
    id         serial primary key not null,
    first_name text,
    last_name  text,
    inn        text,
    hired      timestamp
);

create table person (
    id       serial primary key not null,
    login    text,
    password text
);

create table employee_person (
    employee_id int references employee (id),
    person_id   int references person (id),
    primary key (employee_id, person_id)
);