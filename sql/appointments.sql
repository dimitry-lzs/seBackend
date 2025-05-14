CREATE TABLE appointments (
	slotID INTEGER REFERENCES availabilities(availabilityID),
	status VARCHAR(30),
	reason TEXT,
	patientID INTEGER REFERENCES users(id),
	doctorID INTEGER REFERENCES users(id),
	appointmentID INTEGER PRIMARY KEY
); 