package com.kamaii.partner.ui.models;

public class CancelReasonModel {

    String reason = "";
    String cancel_reason_id="";
    String role_id="";
    String cancel_reason="";
    String status="";

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCancel_reason_id() {
        return cancel_reason_id;
    }

    public void setCancel_reason_id(String cancel_reason_id) {
        this.cancel_reason_id = cancel_reason_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
