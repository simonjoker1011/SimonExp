	drop table if exists CurrencyRate;

	create table CurrencyRate (
	   currCode INTEGER(3),    
       rate VARCHAR(20),
       cashspot VARCHAR(20),
       rateDate DATE,
       currName VARCHAR(20),
       price FLOAT(10,5),
       fetchUrl VARCHAR(200),
       PRIMARY KEY(currCode, rate, cashspot, rateDate)
	);
	
	
	drop table if exists Department;

	create table Department (
	       id bigint not null,
	        name varchar(255),
	        primary key (id)
	);

    drop table if exists Employee;

    create table Employee (
       id bigint not null,
        name varchar(255),
        department_id bigint,
        primary key (id)
    );

    drop table if exists hibernate_sequence;

    create table hibernate_sequence (
       next_val bigint
    );
    
    insert into hibernate_sequence values ( 1 );
