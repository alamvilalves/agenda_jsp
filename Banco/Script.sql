create database dbagenda;
show databases;
use dbagenda;

create table contatos(
	idcon int primary key auto_increment,
    nome varchar(100) not null,
    fone varchar(15) not null,
    email varchar(50) not null
);

show tables;
describe contatos;