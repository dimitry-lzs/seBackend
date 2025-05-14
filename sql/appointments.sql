CREATE TABLE appointments (
	date TIMESTAMP,
	timeFrom TIME,
	timeTo TIME,
	status VARCHAR(30),
	patientID INTEGER REFERENCES users(id),
	doctorID INTEGER REFERENCES users(id),
	appointmentID INTEGER PRIMARY KEY
); 