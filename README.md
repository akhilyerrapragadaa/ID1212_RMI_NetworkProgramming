# ID1212_networkprogramming_RMI
This repo contains implementation in java using RMI, JDBC and MYSQL.

Below are the tables used:

1)Task table for user information:
CREATE TABLE IF NOT EXISTS tasks (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) ,
    file_data BLOB,
    title VARCHAR(255) NULL,
    start_date DATE,
    due_date DATE,
    status TINYINT NULL,
    priority TINYINT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)  ENGINE=INNODB;

2) Files table to store all files
CREATE TABLE IF NOT EXISTS files (
    filename VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255),
    file_data BLOB,
    title VARCHAR(255) NULL,
    start_date DATE,
    due_date DATE,
    status TINYINT NULL,
    priority TINYINT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES tasks(username) 
)  ENGINE=INNODB;
