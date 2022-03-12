
package com.kamaii.partner.ui.models.newbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {

    @SerializedName("slot_start_time")
    @Expose
    private String slotStartTime;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("qty")
    @Expose
    private String qty;

    public String getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(String slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}
