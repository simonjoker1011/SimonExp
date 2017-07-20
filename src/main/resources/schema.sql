  drop table if exists CurrencyConfig;
  
  create table CurrencyConfig (
         id INTEGER NOT NULL,
         updateDate DATE
  );
  
  insert into CurrencyConfig values (1,NOW());
  
  insert into CurrencyConfig values (1,str_to_date('2010-01-01','%Y-%m-%d'));

  drop table if exists CurrencyInfo;

  create table CurrencyInfo (
       id INTEGER NOT NULL,
       currCode INTEGER(3),    
       rate VARCHAR(20),
       cashspot VARCHAR(20),      
       currName VARCHAR(20),
       historyHigh FLOAT(10,5),
       historyLow FLOAT(10,5),
       decadeHigh FLOAT(10,5),
       decadeLow FLOAT(10,5),
       fourYearHigh FLOAT(10,5),
       fourYearLow FLOAT(10,5),
       yearHigh FLOAT(10,5),
       yearLow FLOAT(10,5),
       halfYearHigh FLOAT(10,5),
       halfYearLow FLOAT(10,5),
       seasonHigh FLOAT(10,5),
       seasonLow FLOAT(10,5),
       monthHigh FLOAT(10,5),
       monthLow FLOAT(10,5),
       weekHigh FLOAT(10,5),
       weekLow FLOAT(10,5),
       dateBase DATE,
       PRIMARY KEY(currCode, rate, cashspot)
  );

	drop table if exists CurrencyData;

	create table CurrencyData (
       id INTEGER NOT NULL,
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
