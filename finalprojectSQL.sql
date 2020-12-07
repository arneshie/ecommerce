create table users(
    id int,
    username varchar(255)  NOT NULL ,
    password varchar(255),
    primary key(username)
);

CREATE TABLE Address (
id INT UNSIGNED NOT NULL AUTO_INCREMENT,
street VARCHAR(100) NOT NULL,
province VARCHAR(20) NOT NULL,
country VARCHAR(20) NOT NULL,
zip VARCHAR(20) NOT NULL,
phone VARCHAR(20),
PRIMARY KEY(id)
);

create table Cart(
  book varchar(255),
  username varchar(255) NOT NULL,
  quantity int,
  constraint fk_user_cart foreign key (username) references Users(username)
);

create table Orders(
  id int,
  username varchar(255),
  status int,
  constraint fk_user_order foreign key (username) references Users(username)
);

INSERT INTO Book (bid, title, price, category) VALUES ('b001', 'Little Prince', 20, 'Fiction');
INSERT INTO Book (bid, title, price, category) VALUES ('b002','Physics', 201, 'Science');
INSERT INTO Book (bid, title, price, category) VALUES ('b003','Mechanics' ,100,'Engineering');

create table Book (
    bid VARCHAR(20) NOT NULL,
    title VARCHAR(60) NOT NULL,
    price INT NOT NULL,
    category varchar(60) NOT NULL,
    PRIMARY KEY(bid)
);