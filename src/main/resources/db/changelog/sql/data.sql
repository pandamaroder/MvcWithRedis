
--changeset olga.kukushkina:2024.05.12:news.insert
insert into demo.categories (name, created_at, updated_at)
values ('Science Fiction', now(), now());

--changeset olga.kukushkina:2024.05.12:news.insert
insert into demo.books (title, content, category_id, created_at, updated_at)
values ('Dune', 'A science fiction novel by Frank Herbert.', (select id from demo.categories where name = 'Science Fiction'), now(), now());



