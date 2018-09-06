package xyz.sleepygamers.admincanteen_app;

public class EndPoints {

    private static final String ROOT_URL = "http://sleepygamers.xyz/tatapower/";
    public static final String ADDMENU_URL = ROOT_URL + "foodmenu.php?apicall=add";
    public static final String GET_FOODMENU = ROOT_URL + "foodmenu.php?apicall=get&category=";
    public static final String DELETE_FOODMENU = ROOT_URL + "foodmenu.php?apicall=delete&id=";
    public static final String UPDATE_FOODMENU = ROOT_URL + "foodmenu.php?apicall=update&id=";
    public static final String ORDERS_PENDING = ROOT_URL + "order.php?apicall=getorders&type=pending";
}
