
create table customer(
    id int not null auto_increment primary key,
    firstName varchar(100) not null,
    lastName varchar(100) not null,
    mail varchar(100) not null,
    address varchar(100) not null
);

create table bankAccount(
    id int not null auto_increment primary key,
    accountNumber varchar(100) not null,
    balance double not null,
    type varchar(100) not null,
    cardNumber varchar(16),
    customer int not null,
    CONSTRAINT FK_customerId_account FOREIGN KEY (customer) REFERENCES customer(id)
);

create table product(
    id int not null auto_increment primary key,
    name varchar(100) not null,
    description varchar(100) not null,
    price double not null,
    stock int not null,
    category varchar(45) not null
);

create table orders(
    id int not null auto_increment primary key,
    totalAmount double not null,
    datePlaced date not null,
    account int not null,
    customer int not null,
    CONSTRAINT FK_accountId_order FOREIGN KEY (account) REFERENCES bankAccount(id),
    CONSTRAINT FK_customerId_order FOREIGN KEY (customer) REFERENCES customer(id)
);

create table orderItem(
    id int not null auto_increment primary key,
   quantity int not null,
   price double not null,
   product int not null,
   orders int not null,
   CONSTRAINT FK_productId_item FOREIGN KEY (product) REFERENCES product(id),
   CONSTRAINT FK_orderId_item FOREIGN KEY (orders) REFERENCES orders(id)
);


create table cart(
    id int not null auto_increment primary key,
    totalAmount double not null,
    customer int not null,
    CONSTRAINT FK_customerId_cart FOREIGN KEY (customer) REFERENCES customer(id)
);

