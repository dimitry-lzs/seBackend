package com.softwareengineering.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwareengineering.dto.DiagnosisBody;
import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.Availability;
import com.softwareengineering.models.Diagnosis;
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
                appointmentData.put("patient_avatar", patient.getString("avatar"));
                appointmentData.put("patient_phone", patient.getString("phone"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_timefrom", availability.getString("timeFrom"));
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
                appointmentData.put("doctor_avatar", doctor.getString("avatar"));
                appointmentData.put("doctor_specialty", doctor.getString("speciality"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_timefrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static void setAppointment(int patientID, int doctorID, int slotID, Status status, String reason) {
        Appointment appointment = new Appointment();
        appointment.set("patientID", patientID);
        appointment.set("doctorID", doctorID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        if (reason != null && !reason.isEmpty()) {
            appointment.set("reason", reason);
        }
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

    public static void completeAppointment(int appointmentID) {
        // Check if appointment exists
        Appointment appointment = Appointment.findFirst("appointmentID = ?", appointmentID);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }

        // Check if there's at least one diagnosis for this appointment
        List<Diagnosis> diagnoses = DiagnosesService.getAppointmentDiagnoses(appointmentID);
        if (diagnoses.isEmpty()) {
            throw new IllegalStateException("Cannot complete appointment without at least one diagnosis.");
        }

        int rowsUpdated = Appointment.update("status = ?",
                "appointmentID = ?",
                Status.COMPLETED.toString(),
                appointmentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }
        // Note: We don't free the availability slot when completing (unlike cancelling)
        // as the appointment actually took place
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
            appointmentData.put("doctor_id", doctor.getInteger("id"));
            appointmentData.put("doctor_avatar", doctor.getString("avatar"));
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
            appointmentData.put("slot_timefrom", availability.getString("timeFrom"));
        }

        // Get all diagnoses for this appointment
        List<Diagnosis> diagnoses = DiagnosesService.getAppointmentDiagnoses(appointmentID);
        List<Map<String, Object>> diagnosesData = new ArrayList<>();

        for (Diagnosis diagnosis : diagnoses) {
            DiagnosisBody diagnosisBody = new DiagnosisBody(diagnosis);
            Map<String, Object> diagnosisMap = new java.util.HashMap<>();
            diagnosisMap.put("decease", diagnosisBody.decease);
            diagnosisMap.put("details", diagnosisBody.details);
            diagnosesData.add(diagnosisMap);
        }

        appointmentData.put("diagnoses", diagnosesData);

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
                appointmentData.put("patient_avatar", patient.getString("avatar"));
                appointmentData.put("patient_phone", patient.getString("phone"));
            }

            // Get availability information
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);
            if (availability != null) {
                appointmentData.put("slot_timefrom", availability.getString("timeFrom"));
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
                appointmentData.put("slot_timefrom", availability.getString("timeFrom"));
            }

            result.add(appointmentData);
        }

        return result;
    }

    public static List<Map<String, Object>> getDoctorAppointmentsAfterDate(int doctorID, Timestamp date) {
        List<Appointment> appointments = Appointment.where("doctorID = ?", doctorID);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);

            if (availability != null) {
                // Check if the appointment time is after the requested date
                String timeFromStr = availability.getString("timeFrom");

                try {
                    Timestamp appointmentTime = Timestamp.valueOf(timeFromStr);

                    // Only include appointments after the specified date
                    if (appointmentTime.after(date)) {
                        Map<String, Object> appointmentData = appointment.toMap();

                        // Get patient information - only name and phone
                        int patientID = appointment.getInteger("patientID");
                        User patient = User.findFirst("id = ?", patientID);
                        if (patient != null) {
                            appointmentData.put("patient_name", patient.getString("fullName"));
                            appointmentData.put("patient_avatar", patient.getString("avatar"));
                            appointmentData.put("patient_phone", patient.getString("phone"));
                        }

                        // Add availability information
                        appointmentData.put("slot_timefrom", timeFromStr);

                        result.add(appointmentData);
                    }
                } catch (IllegalArgumentException e) {
                    // Handle case where timeFrom is not in the expected format
                    System.err
                            .println("Invalid time format for appointment " + appointment.getId() + ": " + timeFromStr);
                }
            }
        }

        return result;
    }

    public static List<Map<String, Object>> getPatientAppointmentsAfterDate(int patientID, Timestamp date) {
        List<Appointment> appointments = Appointment.where("patientID = ?", patientID);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            int slotID = appointment.getInteger("slotID");
            Availability availability = Availability.findFirst("availabilityID = ?", slotID);

            if (availability != null) {
                // Check if the appointment time is after the requested date
                String timeFromStr = availability.getString("timeFrom");

                try {
                    Timestamp appointmentTime = Timestamp.valueOf(timeFromStr);

                    // Only include appointments after the specified date
                    if (appointmentTime.after(date)) {
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

                        // Add availability information
                        appointmentData.put("slot_timefrom", timeFromStr);

                        result.add(appointmentData);
                    }
                } catch (IllegalArgumentException e) {
                    // Handle case where timeFrom is not in the expected format
                    System.err
                            .println("Invalid time format for appointment " + appointment.getId() + ": " + timeFromStr);
                }
            }
        }

        return result;
    }
}
