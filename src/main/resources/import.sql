-- Insert role
insert into role(name) values ('ROLE_USER');
insert into role(name) values ('ROLE_ADMIN');

-- Insert users
insert into user(username, enabled, password, role_id) values ('user', true, '$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u', 1);
insert into user(username, enabled, password, role_id) values ('user2', true, '$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u', 1);