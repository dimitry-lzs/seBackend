package com.softwareengineering.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.Availability;
import com.softwareengineering.models.User;
import com.softwareengineering.models.enums.Status;

public class AppointmentsService {
    public static List<Map<String, Object>> getDoctorAppointments(int doctorID) {
        List<Appointment> appointments = Appointment.where("doctorID = ?", doctorID);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get patient information - only name and telephone
            int patientID = appointment.getInteger("patientID");
            User patient = User.findFirst("id = ?", patientID);
            if (patient != null) {
                appointmentData.put("patient_name", patient.getString("fullName"));
                appointmentData.put("patient_phone", patient.getString("phone"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static List<Map<String, Object>> getDoctorAppointments(int doctorID, Status status) {
        List<Appointment> appointments = Appointment.where("doctorID = ? AND status >= ?", doctorID,
                status.toString());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get patient information - only name and phone
            int patientID = appointment.getInteger("patientID");
            User patient = User.findFirst("id = ?", patientID);
            if (patient != null) {
                appointmentData.put("patient_name", patient.getString("fullName"));
                appointmentData.put("patient_phone", patient.getString("phone"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID) {
        List<Appointment> appointments = Appointment.where("patientID = ?", patientID);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get doctor information - only name and speciality
            int doctorID = appointment.getInteger("doctorID");
            User doctor = User.findFirst("id = ?", doctorID);
            if (doctor != null) {
                appointmentData.put("doctor_name", doctor.getString("fullName"));
                appointmentData.put("doctor_specialty", doctor.getString("speciality"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID, Status status) {
        List<Appointment> appointments = Appointment.where("patientID = ? AND status >= ?", patientID,
                status.toString());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get doctor information - only name and specialty
            int doctorID = appointment.getInteger("doctorID");
            User doctor = User.findFirst("id = ?", doctorID);
            if (doctor != null) {
                appointmentData.put("doctor_name", doctor.getString("fullName"));
                appointmentData.put("doctor_specialty", doctor.getString("speciality"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.saveIt();
        AvailabilitiesService.updateAvailability(slotID, false);
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status, String reason) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.set("reason", reason);
        appointment.saveIt();
        AvailabilitiesService.updateAvailability(slotID, false);
    }

    public static void cancelAppointment(int appointmentID) {
        int rowsUpdated = Appointment.update("status = ?",
                "appointmentID = ?",
                Status.CANCELLED.toString(),
                appointmentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        } else {
            AvailabilitiesService.updateAvailability(
                    Appointment.findFirst("appointmentID = ?", appointmentID).getInteger("slotID"), true);
        }
    }

    public static Map<String, Object> getAppointmentDetails(int appointmentID) {
        Appointment appointment = Appointment.findFirst("appointmentID = ?", appointmentID);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }
        Map<String, Object> appointmentData = appointment.toMap();

        // Get doctor information
        int doctorID = appointment.getInteger("doctorID");
        User doctor = User.findFirst("id = ?", doctorID);
        if (doctor != null) {
            appointmentData.put("doctor_name", doctor.getString("fullName"));
            appointmentData.put("doctor_specialty", doctor.getString("speciality"));
            appointmentData.put("doctor_phone", doctor.getString("phone"));
            appointmentData.put("doctor_email", doctor.getString("email"));
            appointmentData.put("doctor_officeLocation", doctor.getString("officeLocation"));
            appointmentData.put("doctor_bio", doctor.getString("bio"));
            appointmentData.put("doctor_licenceID", doctor.getString("licenceID"));
        }

        // Get availability information
        int slotID = appointment.getInteger("slotID");
        Availability availability = Availability.findFirst("availabilityID = ?", slotID);
        if (availability != null) {
            appointmentData.put("slot_id", availability.getInteger("availabilityID"));
            appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
        }

        return appointmentData;
    }

    public static List<Map<String, Object>> getDoctorAppointmentsByStatuses(int doctorID, List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return new ArrayList<>();
        }

        // Build the query with placeholders for each status
        StringBuilder queryBuilder = new StringBuilder("doctorID = ? AND (");
        List<Object> params = new ArrayList<>();
        params.add(doctorID);

        for (int i = 0; i < statuses.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("status = ?");
            params.add(statuses.get(i).toString());
        }
        queryBuilder.append(")");

        List<Appointment> appointments = Appointment.where(queryBuilder.toString(), params.toArray());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get patient information - only name and phone
            int patientID = appointment.getInteger("patientID");
            User patient = User.findFirst("id = ?", patientID);
            if (patient != null) {
                appointmentData.put("patient_name", patient.getString("fullName"));
                appointmentData.put("patient_phone", patient.getString("phone"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static List<Map<String, Object>> getPatientAppointmentsByStatuses(int patientID, List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return new ArrayList<>();
        }

        // Build the query with placeholders for each status
        StringBuilder queryBuilder = new StringBuilder("patientID = ? AND (");
        List<Object> params = new ArrayList<>();
        params.add(patientID);

        for (int i = 0; i < statuses.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("status = ?");
            params.add(statuses.get(i).toString());
        }
        queryBuilder.append(")");

        List<Appointment> appointments = Appointment.where(queryBuilder.toString(), params.toArray());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentData = appointment.toMap();

            // Get patient information - only name and phone
            int doctorID = appointment.getInteger("doctorID");
            User doctor = User.findFirst("id = ?", doctorID);
            if (doctor != null) {
                appointmentData.put("doctor_name", doctor.getString("fullName"));
                appointmentData.put("doctor_specialty", doctor.getString("speciality"));
                appointmentData.put("doctor_phone", doctor.getString("phone"));
                appointmentData.put("doctor_email", doctor.getString("email"));
                appointmentData.put("doctor_officeLocation", doctor.getString("officeLocation"));
                appointmentData.put("doctor_bio", doctor.getString("bio"));
                appointmentData.put("doctor_licenceID", doctor.getString("licenceID"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_id", availability.getInteger("availabilityID"));
                appointmentData.put("slot_timeFrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }
}
