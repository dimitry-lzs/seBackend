# Doctor Appointments App: Technical Requirements

### **Task Analysis for the Patient-Doctor Appointment Booking App**  

#### **1. User Roles & Actions**  
- **Doctor (Ιατρός)**
  - Register to the system with personal details (licenceID, full name, specialty, contact details (email,phone), bio, office location, ratings, password).  
  - Manage availability (set available days/hours for appointments).  
  - View scheduled appointments. (possible integration with calendar)
  - Cancel an appointment if necessary.
  - View ratings.
  - (Send messages to patients - LLM)


- **Patient (Ασθενής)**  
  - Register using AMKA, full name, contact details (email,phone) and a password.  
  - Log in to the system.  
  - Search for doctors by specialty and location.  
  - View available doctors.  
  - Check doctor’s availability (sooner to later).  
  - Book an appointment by selecting a date/time and stating the reason (text field).
  - Cancel a booked appointment.  
  - View scheduled appointments (weekly/monthly).
  - View appointment history (and also rate).

#### **2. Core Functionalities & Task Breakdown**  

##### **User Registration & Authentication**  
- Patient enters AMKA, full name, contact details (email,phone) to register.
- Patient enters AMKA and password to login.
- Doctor enters licenceID, full name, specialty, contact details (email,phone), bio, office location, ratings to register.  
- Doctor enters licenceID and password to login.

##### **Doctor Management & Availability**   
- Doctors set their available time slots.  
- Doctors can modify availability.

##### **Searching for Doctors & Viewing Availability (for patient)**  
- Patient selects specialty.  
- Patient selects location (from pre-selected ones).  
- System displays a list of available doctors (use filters).  
- Patient clicks on a doctor of his choosing.  

##### **Appointment Booking & Management**  
- Patient selects an available time slot.  
- Patient provides a reason for the appointment (optional).  
- System confirms and stores the booking.  
- Patient can cancel a booked appointment.  
- Patient views scheduled appointments (weekly/monthly).

##### **Appointment Display & Notifications**  
- System provides an overview of upcoming appointments.  
- Patients receive notifications/reminders for appointments (optional).  

#### **3. System Requirements & Considerations**  
- **Database**: Store doctor and patient information, appointments, and availability.  
- **Authentication & Security**: Protect AMKA and user credentials. (salted + hashed) 
- **User Interface**: Intuitive UI for booking and managing appointments.  
- **Scalability**: Handle multiple users simultaneously.  