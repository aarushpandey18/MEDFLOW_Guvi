CREATE DATABASE IF NOT EXISTS medflow;
USE medflow;

CREATE TABLE IF NOT EXISTS patients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  age INT NOT NULL,
  phone VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS doctors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  specialization VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_time DATETIME NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
  FOREIGN KEY (patient_id) REFERENCES patients(id),
  FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  UNIQUE KEY uq_doctor_slot (doctor_id, appointment_time)
);

INSERT IGNORE INTO doctors (name, email, specialization) VALUES
 ('Dr. Sharma','sharma@medflow.com','Cardiology'),
 ('Dr. Verma','verma@medflow.com','General Physician');
