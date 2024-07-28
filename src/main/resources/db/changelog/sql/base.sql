
--changeset olga.kukushkina:2024.05.12:books.sequence
create sequence if not exists demo.categories_seq;

--changeset olga.kukushkina:2024.05.12:books.table
create table if not exists demo.categories (
    id bigint primary key default nextval('demo.categories_seq'),
    name varchar(255) not null,
    created_at timestamp not null,
        updated_at timestamp
);

--changeset olga.kukushkina:2024.05.12:books.sequence
create sequence if not exists demo.books_seq;

--changeset olga.kukushkina:2024.05.12:books.table
create table if not exists demo.books (
    id bigint primary key default nextval('demo.books_seq'),
    title varchar(255) not null,
    content text,
    category_id bigint,
    foreign key (category_id) references demo.categories (id),
    created_at timestamp not null,
        updated_at timestamp
);



