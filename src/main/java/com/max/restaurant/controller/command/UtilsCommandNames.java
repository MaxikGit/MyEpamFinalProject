package com.max.restaurant.controller.command;

public class UtilsCommandNames {
//    command names are the ones, followed after the "action" in GET or POST: action=command
    public static final String ACTION = "action";
    public static final String LOGIN = "login";
    public static final String SIGN_UP = "sign_up";
    public static final String CATEGORY = "category";
    public static final String ORDER = "order";
    public static final String MANAGEMENT = "management";

    public static final String ORDER_EDIT = "orderEdit";
    public static final String SORT_DISHES = "sortDishes";
    public static final String CATEGORY_ID = "categoryId";
    public static final String SETTINGS_CATTEGORY = "settingsCategory";
    public static final String DISH = "dish";

//    attrs are mapping variables JAVA.var = "JSP.attr": SUCCESS_ATTR = "success";
    public static final String SUCCESS_ATTR = "success";
    public static final String UNSUCCESS_ATTR = "unsuccess";
    public static final String UNSUCCESS_MSG = "Sorry, you don`t have an account yet";
    public static final String UNSUCCESS_MSG2 = "Sorry, there was a mistake in input";

//    container attr
    public static final String LOGGED_USER_ATTR = "loggedUser";
    public static final String CATEGORY_LIST_ATTR = "categoryNames";
    public static final String DISH_IDS_LIST_ATTR = "dishIds";
    public static final String DISH_LIST_ATTR = "dishesNames";
    public static final String CUSTOM_LIST_ATTR = "customsList";
    public static final String ORDER_LIST_ATTR = "orderNames";
    public static final String STATUS_LIST_ATTR = "statusNames";

    public static final String ORDER_MANAGEMENT_ATTR = "orderManagement";

//     stand alone values attr
    public static final String VALUE_ATTR = "value";
    public static final String STATUS_ATTR = "statusSelect";
    public static final String ORDER_TOTAL_COST_ATTR = "totalCost";
    public static final String ORDER_TOTAL_INPROGRESS_ATTR = "totalOrdersCost";
    public static final String DEL_FROM_ORDER_ATTR = "deleteId";
    public static final String QUANTITY_ATTR = "quantity";

}
