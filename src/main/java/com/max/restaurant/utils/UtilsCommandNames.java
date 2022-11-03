package com.max.restaurant.utils;

public class UtilsCommandNames {
//    command names are the ones, followed after the "action" in GET or POST: action=command

    public static final String FIRST_START = "ServletController?action=category";
    public static final String MANAGEMENT_COMM = "ServletController?action=management";
    public static final String ACTION = "action";
    public static final String LOGIN = "login";
    public static final String SIGN_UP = "sign_up";
    public static final String CATEGORY = "category";
    public static final String MANAGEMENT = "management";
    public static final String MANAGER_EDIT_ORDER = "orderEditManagement";
    public static final String LANGUAGE = "language";
    public static final String PDF = "pdf";
    public static final String EDIT_ORDER = "orderEdit";
    public static final String SORT_DISHES = "sortDishes";


//    attrs are mapping variables JAVA.var = "JSP.attr": SUCCESS_ATTR = "success";
    public static final String CATEGORY_ID = "categoryId";
    public static final String UNSUCCESS_ATTR = "unsuccess";
    public static final String UNSUCCESS_MSG = "login.sorry";
    public static final String UNSUCCESS_MSG2 = "login.sorry2";
    public static final String UNSUCCESS_MSG3 = "signup.sorry";
    public static final String UNSUCCESS_MSG4 = "login.sorry3";

//    container attr
    public static final String LOGGED_USER_ATTR = "loggedUser";
    public static final String CATEGORY_LIST_ATTR = "categoryNames";
    public static final String DISH_IDS_LIST_ATTR = "dishIds";
    public static final String DISH_LIST_ATTR = "dishesNames";
    public static final String CUSTOM_LIST_ATTR = "customsList";
    public static final String ORDER_MAP_ATTR = "orderNames";
    public static final String STATUS_LIST_ATTR = "statusNames";

    public static final String MANAGEMENT_ORDERDATA_LIST_ATTR = "orderManagement";
    public static final String MANAGEMENT_ORDERDATA_ATTR = "orderDataManagement";

//     stand alone values attr
    public static final String INPROGRESS_ATTR = "inProgress";
    public static final String VALUE_ATTR = "value";
    public static final String STATUS_ATTR = "statusSelect";
    public static final String ORDER_TOTAL_COST_ATTR = "totalCost";
    public static final String ORDER_TOTAL_INPROGRESS_ATTR = "totalOrdersCost";
    public static final String DEL_FROM_ORDER_ATTR = "deleteId";
    public static final String QUANTITY_ATTR = "quantity";
    public static final String ORDER_ACCEPT_ATTR = "accepted";
    public static final String SORT_ATTR = "sort";

    public static final String PAGE_ATTR = "pageNum";
    public static final String PAGES_MAX_ATTR = "pagesMax";
    public static final String PAGES_MIN_ATTR = "pagesMin";
    public static final String TOTAL_PAGES_ATTR = "pagesTotal";
    public static final String RECS_PER_PAGE_ATTR = "pagesRecs";
    public static final String LANG_ATTR = "lang";

    public static final String ENG_ATTR = "en";
    public static final String UKR_ATTR = "uk";

   }
