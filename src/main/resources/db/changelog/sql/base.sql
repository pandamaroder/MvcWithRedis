
--changeset olga.kukushkina:2024.05.12:news.sequence
create sequence if not exists demo.categories_seq;

--changeset olga.kukushkina:2024.05.12:news.table
create table if not exists demo.categories (
    id bigint primary key default nextval('demo.categories_seq'),
    name varchar(255) not null,
    created_at timestamp not null,
        updated_at timestamp
);

--changeset olga.kukushkina:2024.05.12:news.sequence
create sequence if not exists demo.book_seq;

--changeset olga.kukushkina:2024.05.12:news.table
create table if not exists demo.books (
    id bigint primary key default nextval('demo.news_seq'),
    title varchar(255) not null,
    content text,
    category_id bigint,
    foreign key (user_id) references demo.users (id),
    foreign key (category_id) references demo.categories (id),
    created_at timestamp not null,
        updated_at timestamp
);



