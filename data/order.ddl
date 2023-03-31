CREATE TABLE Employee (
   EmployeeId    char(5)        NOT NULL,
   EmployeeName  varchar(50),
   Salary        decimal (9,2),
   SupervisorId  char(5),
   PRIMARY KEY (EmployeeId),
   FOREIGN KEY (SupervisorId) REFERENCES Employee (EmployeeId)
);

CREATE TABLE Customer (
   CustomerId    char(5)        NOT NULL,
   CustomerName  varchar(50),
   PRIMARY KEY (CustomerId)
);

CREATE TABLE Product (
   ProductId     char(5)        NOT NULL,
   ProductName   varchar(50),
   ListPrice     decimal (9,2),
   PRIMARY KEY (ProductId)
);

CREATE TABLE Orders (
   OrderId       char(5)        NOT NULL,
   OrderDate     datetime,
   CustomerId    char(5),
   EmployeeId    char(5),
   Total         decimal (9,2),
   PRIMARY KEY (OrderId),
   FOREIGN KEY (CustomerId) REFERENCES Customer (CustomerId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (EmployeeId) REFERENCES Employee (EmployeeId)
);

CREATE TABLE OrderedProduct (
   OrderId       char(5)        NOT NULL,
   ProductId     char(5) 	NOT NULL,
   Quantity      integer,
   Price         decimal (9,2),
   PRIMARY KEY (OrderId, ProductId),
   FOREIGN KEY (OrderId) REFERENCES Orders (OrderId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (ProductId) REFERENCES Product (ProductId)
);