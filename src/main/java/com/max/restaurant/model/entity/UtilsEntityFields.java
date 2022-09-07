package com.max.restaurant.model.entity;

import com.max.restaurant.exceptions.DAOException;

public class UtilsEntityFields {
	public static final String USER_TABLE = "user";
	public static final String CATEGORY_TABLE = "category";
	public static final String CUSTOM_TABLE = "custom";
	public static final String CUSTOM_HAS_DISH_TABLE = "custom_has_dish";
	public static final String DISH_TABLE = "dish";
	public static final String ROLE_TABLE = "role";
	public static final String STATUS_TABLE = "status";

	public static final String USER_ID = "id";
	public static final String USER_NAME = "name";
	public static final String USER_EMAIL = "email";
	public static final String USER_LASTNAME = "last_name";
	public static final String USER_PASSWORD = "password";
	public static final String USER_DETAILS = "details";
	public static final String USER_DETAILS_DEFAULT = "none";
	public static final String USER_ROLE_ID = "role_id";

	public static final String ROLE_ID = USER_ID;
	public static final String ROLE_NAME = USER_NAME;
	public static final int ROLE_CLIENT_ID = 2;
	public static final int ROLE_MANAGER_ID = 1;

	public static final String STATUS_ID = USER_ID;
	public static final String STATUS_NAME = USER_NAME;
	public static final String STATUS_DETAILS = USER_DETAILS;

	public static final String CATEGORY_ID = USER_ID;
	public static final String CATEGORY_NAME = USER_NAME;

	public static final String CUSTOM_ID = USER_ID;
	public static final String CUSTOM_COST = "cost";
	public static final String CUSTOM_TIME = "create_time";
	public static final String CUSTOM_USER_ID = "user_id";
	public static final String CUSTOM_STATUS_ID = "status_id";

	public static final String DISH_ID = USER_ID;
	public static final String DISH_NAME = USER_NAME;
	public static final String DISH_PRICE = "price";
	public static final String DISH_DETAILS = USER_DETAILS;
	public static final String DISH_CATEGORY_ID = "category_id";
	public static final String DISH_IMAGE_PATH = "image_path";

	public static final String CUSTOMHASDISH_C_ID = "custom_id";
	public static final String CUSTOMHASDISH_D_ID = "dish_id";
	public static final String CUSTOMHASDISH_COUNT = "count";
	public static final String CUSTOMHASDISH_PRICE = DISH_PRICE;

	private static final String USER_CLASS = User.class.getSimpleName();
	private static final String CATEGORY_CLASS = Category.class.getSimpleName();
	private static final String CUSTOM_CLASS = Custom.class.getSimpleName();
	private static final String CUSTOM_HAS_DISH_CLASS = CustomHasDish.class.getSimpleName();
	private static final String DISH_CLASS = Dish.class.getSimpleName();
	private static final String ROLE_CLASS = Role.class.getSimpleName();
	private static final String STATUS_CLASS = Status.class.getSimpleName();

	public static String getTableNameByClass(String simpleClassName) throws DAOException {
		String result;
		if (simpleClassName.equals(USER_CLASS))
			result = USER_TABLE;
		else if (simpleClassName.equals(CATEGORY_CLASS))
			result = CATEGORY_TABLE;
		else if (simpleClassName.equals(CUSTOM_CLASS))
			result = CUSTOM_TABLE;
		else if (simpleClassName.equals(CUSTOM_HAS_DISH_CLASS))
			result = CUSTOM_HAS_DISH_TABLE;
		else if (simpleClassName.equals(DISH_CLASS))
			result = DISH_TABLE;
		else if (simpleClassName.equals(ROLE_CLASS))
			result = ROLE_TABLE;
		else if (simpleClassName.equals(STATUS_CLASS))
			result = STATUS_TABLE;
		else throw new DAOException("wrong SQL expression");
		return result;
	}

}
