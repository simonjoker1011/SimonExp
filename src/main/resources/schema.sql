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
