package xyz.sleepygamers.admincanteen_app.models;

public class Order {
    private int id, price, user_id, user_roomno, user_uid;
    private String order_details, order_date, order_type, delivery_type, order_status, user_name;

    public Order() {
    }

    public Order(int id, String order_details, String order_date, int price, String order_type, String delivery_type, String order_status, int user_id, String user_name, int user_roomno, int user_uid) {
        this.id = id;
        this.price = price;
        this.user_id = user_id;
        this.user_roomno = user_roomno;
        this.user_uid = user_uid;
        this.order_details = order_details;
        this.order_date = order_date;
        this.order_type = order_type;
        this.delivery_type = delivery_type;
        this.order_status = order_status;
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_roomno() {
        return user_roomno;
    }

    public void setUser_roomno(int user_roomno) {
        this.user_roomno = user_roomno;
    }

    public int getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(int user_uid) {
        this.user_uid = user_uid;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
