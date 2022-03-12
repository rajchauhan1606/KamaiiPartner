package com.kamaii.partner.DTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by VARUN on 01/01/19.
 */
public class ArtistBooking implements Serializable {


    String id = "";
    String user_id = "";
    String artist_id = "";
    String booking_time = "";
    String start_time = "";
    String description = "";
    String category_price = "";
    String booking_date = "";
    String end_time = "";
    String price = "";
    String status = "";
    String booking_flag = "";
    String decline_by = "";
    String time_zone = "";
    String decline_reason = "";
    String booking_timestamp = "";
    String created_at = "";
    String updated_at = "";
    String artistImage = "";
    String category_name = "";
    String artistName = "";
    String artistLocation = "";
    String userName = "";
    String address = "";
    String ava_rating = "";
    String c_latitude = "";
    String c_longitude = "";
    String working_min = "";
    String userImage = "";
    String currency_type = "";
    String booking_type = "";
    boolean isSection = false;
    String section_name = "";
    String latitude = "";
    String longitude = "";
    String location_status = "";
    String userMobile = "";
    String image = "";
    String approxdatetime = "";
    String destinationaddress = "";
    String km = "";
    String second = "";
    String starttimer = "";
    String distance_time = "";
    String pay_type = "";
    String rider_flag = "";
    String rider_order = "";
    String preparation_time = "";
    String preparation_time1 = "";
    String rider_arrival_time = "";
    String rider_waiting_time = "";
    String rider_name = "";
    String mybooking_date = "";
    String product_image = "";
    String short_description = "";
    String servce_charge_txt = "";
    String servce_charge_tracker = "";
    String itemtotal = "";
    String servicecharge = "";
    String ServiceText = "";
    String ServiceTextstatus = "";
    String destinationaddress2 = "";
    String ismap = "";
    String preparation_status = "";
    String pickup_status = "";
    String netpay = "";
    String actuall_collect = "";
    String helpline_number = "";
    String ridertotalkm = "";
    String orderslotno = "";
    String check_artist_preparation = "";
    String getdeliveryflag = "";
    String order_no = "";
    String order_count = "";
    String cooking_instruction = "";
    String request_for_rider_status = "";
    String partnertotal = "";
    String lunch_dinner = "";
    String partercontact = "";
    String ridercontact = "";
    String hold_order = "";
    String delay_msg = "";

    String ordertime = "";
    String deliverytime= "";
    String new_preparation_time = "";
    String new_order_slot_key = "";
    String order_disable = "";
    String customer_booking_flag = "";
    String rider_booking_flag = "";
    String order_no2 = "";
    String pslotstarttime = "";
    String pslotendtime = "";


    public String getPslotstarttime() {
        return pslotstarttime;
    }

    public void setPslotstarttime(String pslotstarttime) {
        this.pslotstarttime = pslotstarttime;
    }

    public String getPslotendtime() {
        return pslotendtime;
    }

    public void setPslotendtime(String pslotendtime) {
        this.pslotendtime = pslotendtime;
    }

    public String getOrder_no2() {
        return order_no2;
    }

    public void setOrder_no2(String order_no2) {
        this.order_no2 = order_no2;
    }

    public String getCustomer_booking_flag() {
        return customer_booking_flag;
    }

    public void setCustomer_booking_flag(String customer_booking_flag) {
        this.customer_booking_flag = customer_booking_flag;
    }

    public String getRider_booking_flag() {
        return rider_booking_flag;
    }

    public void setRider_booking_flag(String rider_booking_flag) {
        this.rider_booking_flag = rider_booking_flag;
    }

    public String getOrder_disable() {
        return order_disable;
    }

    public void setOrder_disable(String order_disable) {
        this.order_disable = order_disable;
    }

    public String getNew_order_slot_key() {
        return new_order_slot_key;
    }

    public void setNew_order_slot_key(String new_order_slot_key) {
        this.new_order_slot_key = new_order_slot_key;
    }

    public String getNew_preparation_time() {
        return new_preparation_time;
    }

    public void setNew_preparation_time(String new_preparation_time) {
        this.new_preparation_time = new_preparation_time;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(String deliverytime3) {
        this.deliverytime= deliverytime3;
    }

    public String getDelay_msg() {
        return delay_msg;
    }

    public void setDelay_msg(String delay_msg) {
        this.delay_msg = delay_msg;
    }

    public String getHold_order() {
        return hold_order;
    }

    public void setHold_order(String hold_order) {
        this.hold_order = hold_order;
    }

    public String getPartercontact() {
        return partercontact;
    }

    public void setPartercontact(String partercontact) {
        this.partercontact = partercontact;
    }

    public String getRidercontact() {
        return ridercontact;
    }

    public void setRidercontact(String ridercontact) {
        this.ridercontact = ridercontact;
    }

    public String getLunch_dinner() {
        return lunch_dinner;
    }

    public void setLunch_dinner(String lunch_dinner) {
        this.lunch_dinner = lunch_dinner;
    }

    public String getPartnertotal() {
        return partnertotal;
    }

    public void setPartnertotal(String partnertotal) {
        this.partnertotal = partnertotal;
    }

    public String getRequest_for_rider_status() {
        return request_for_rider_status;
    }

    public void setRequest_for_rider_status(String request_for_rider_status) {
        this.request_for_rider_status = request_for_rider_status;
    }

    public String getCooking_instruction() {
        return cooking_instruction;
    }

    public void setCooking_instruction(String cooking_instruction) {
        this.cooking_instruction = cooking_instruction;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getGetdeliveryflag() {
        return getdeliveryflag;
    }

    public void setGetdeliveryflag(String getdeliveryflag) {
        this.getdeliveryflag = getdeliveryflag;
    }

    public String getCheck_artist_preparation() {
        return check_artist_preparation;
    }

    public void setCheck_artist_preparation(String check_artist_preparation) {
        this.check_artist_preparation = check_artist_preparation;
    }

    public String getOrderslotno() {
        return orderslotno;
    }

    public void setOrderslotno(String orderslotno) {
        this.orderslotno = orderslotno;
    }

    public String getRidertotalkm() {
        return ridertotalkm;
    }

    public void setRidertotalkm(String ridertotalkm) {
        this.ridertotalkm = ridertotalkm;
    }

    public String getHelpline_number() {
        return helpline_number;
    }

    public void setHelpline_number(String helpline_number) {
        this.helpline_number = helpline_number;
    }

    public String getActuall_collect() {
        return actuall_collect;
    }

    public void setActuall_collect(String actuall_collect) {
        this.actuall_collect = actuall_collect;
    }

    public String getNetpay() {
        return netpay;
    }

    public void setNetpay(String netpay) {
        this.netpay = netpay;
    }

    public String getPickup_status() {
        return pickup_status;
    }

    public void setPickup_status(String pickup_status) {
        this.pickup_status = pickup_status;
    }

    public String getPreparation_status() {
        return preparation_status;
    }

    public void setPreparation_status(String preparation_status) {
        this.preparation_status = preparation_status;
    }

    public String getIsmap() {
        return ismap;
    }

    public void setIsmap(String ismap) {
        this.ismap = ismap;
    }

    public String getDestinationaddress2() {
        return destinationaddress2;
    }

    public void setDestinationaddress2(String destinationaddress2) {
        this.destinationaddress2 = destinationaddress2;
    }

    public String getServiceText() {
        return ServiceText;
    }

    public void setServiceText(String serviceText) {
        ServiceText = serviceText;
    }

    public String getServiceTextstatus() {
        return ServiceTextstatus;
    }

    public void setServiceTextstatus(String serviceTextstatus) {
        ServiceTextstatus = serviceTextstatus;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    public void setServicecharge(String servicecharge) {
        this.servicecharge = servicecharge;
    }

    public String getItemtotal() {
        return itemtotal;
    }

    public void setItemtotal(String itemtotal) {
        this.itemtotal = itemtotal;
    }

    public String getServce_charge_txt() {
        return servce_charge_txt;
    }

    public void setServce_charge_txt(String servce_charge_txt) {
        this.servce_charge_txt = servce_charge_txt;
    }

    public String getServce_charge_tracker() {
        return servce_charge_tracker;
    }

    public void setServce_charge_tracker(String servce_charge_tracker) {
        this.servce_charge_tracker = servce_charge_tracker;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getStarttimer() {
        return starttimer;
    }

    public void setStarttimer(String starttimer) {
        this.starttimer = starttimer;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getMybooking_date() {
        return mybooking_date;
    }

    public void setMybooking_date(String mybooking_date) {
        this.mybooking_date = mybooking_date;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getPreparation_time1() {
        return preparation_time1;
    }

    public void setPreparation_time1(String preparation_time1) {
        this.preparation_time1 = preparation_time1;
    }

    public String getRider_waiting_time() {
        return rider_waiting_time;
    }

    public void setRider_waiting_time(String rider_waiting_time) {
        this.rider_waiting_time = rider_waiting_time;
    }

    public String getRider_arrival_time() {
        return rider_arrival_time;
    }

    public void setRider_arrival_time(String rider_arrival_time) {
        this.rider_arrival_time = rider_arrival_time;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getRider_order() {
        return rider_order;
    }

    public void setRider_order(String rider_order) {
        this.rider_order = rider_order;
    }

    public String getRider_flag() {
        return rider_flag;
    }

    public void setRider_flag(String rider_flag) {
        this.rider_flag = rider_flag;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getDistance_time() {
        return distance_time;
    }

    public void setDistance_time(String distance_time) {
        this.distance_time = distance_time;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getDestinationaddress() {
        return destinationaddress;
    }

    public void setDestinationaddress(String destinationaddress) {
        this.destinationaddress = destinationaddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getApproxdatetime() {
        return approxdatetime;
    }

    public void setApproxdatetime(String approxdatetime) {
        this.approxdatetime = approxdatetime;
    }

    public String getLocation_status() {
        return location_status;
    }

    public void setLocation_status(String location_status) {
        this.location_status = location_status;
    }

    ArrayList<ProductDTO> product = new ArrayList<>();

    public ArrayList<ProductDTO> getProduct() {
        return product;
    }

    ArrayList<String> add_time_array = new ArrayList<>();

    public ArrayList<String> getAdd_time_array() {
        return add_time_array;
    }

    public void setAdd_time_array(ArrayList<String> add_time_array) {
        this.add_time_array = add_time_array;
    }

    public void setProduct(ArrayList<ProductDTO> product) {
        this.product = product;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public String getC_latitude() {
        return c_latitude;
    }

    public String getC_longitude() {
        return c_longitude;
    }

    public void setC_longitude(String c_longitude) {
        this.c_longitude = c_longitude;
    }

    public void setC_latitude(String c_latitude) {
        this.c_latitude = c_latitude;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean section) {
        isSection = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getCategory_price() {
        return category_price;
    }

    public void setCategory_price(String category_price) {
        this.category_price = category_price;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_flag() {
        return booking_flag;
    }

    public void setBooking_flag(String booking_flag) {
        this.booking_flag = booking_flag;
    }

    public String getDecline_by() {
        return decline_by;
    }

    public void setDecline_by(String decline_by) {
        this.decline_by = decline_by;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getDecline_reason() {
        return decline_reason;
    }

    public void setDecline_reason(String decline_reason) {
        this.decline_reason = decline_reason;
    }

    public String getBooking_timestamp() {
        return booking_timestamp;
    }

    public void setBooking_timestamp(String booking_timestamp) {
        this.booking_timestamp = booking_timestamp;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistLocation() {
        return artistLocation;
    }

    public void setArtistLocation(String artistLocation) {
        this.artistLocation = artistLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAva_rating() {
        return ava_rating;
    }

    public void setAva_rating(String ava_rating) {
        this.ava_rating = ava_rating;
    }


    public String getWorking_min() {
        return working_min;
    }

    public void setWorking_min(String working_min) {
        this.working_min = working_min;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }
}
