package helper;

public class AppConfig {
 	//local server
	public static String SERVER = "http://192.168.42.96/";
	//public static String SERVER = "http://192.168.43.54/";
	//
	// public static String SERVER = "http://192.168.8.101/";
		//online server
	//private static String SERVER = "http://www.88radium.com/";

	public static String getLists = SERVER + "list/v1/getLists";
	public static String getPartners = SERVER + "list/v1/getPartners";
	public static String get_category = SERVER + "list/v1/get_category";
	public static String get_list_receipt = SERVER + "list/v1/get_list_receipt";
	public static String getCatItems = SERVER + "list/v1/getCatItems";
	public static String getBasketItems = SERVER + "list/v1/getBasketItems";
	public static String add_Item = SERVER + "list/v1/add_Item";
	public static String add_history = SERVER + "list/v1/add_history";
	public static String getItems = SERVER + "list/v1/getItems";
	public static String updateBasketItem = SERVER + "list/v1/updateBasketItem";
	public static String add_item_basket = SERVER + "list/v1/add_item_basket";
	public static String update_basket_name = SERVER + "list/v1/update_basket_name";
	public static String send_invite = SERVER + "list/v1/send_invite";
	public static String getInvites = SERVER + "list/v1/getInvites";
	public static String update_invite = SERVER + "list/v1/update_invite";
	public static String search = SERVER + "list/v1/search";
	public static String delete_item = SERVER + "list/v1/delete_item";
	public static String getInvitedLists = SERVER + "list/v1/getInvitedLists";
	public static String getMyLists = SERVER + "list/v1/getMyLists";
	public static String registe = SERVER + "list/v1/registe";
	public static String add_basket = SERVER + "list/v1/add_basket";
	public static String delete_list = SERVER + "list/v1/delete_list";
	public static String delete_partner = SERVER + "list/v1/delete_partner";
	public static String get_basket_total = SERVER + "list/v1/get_basket_total";
	public static String getTags = SERVER + "list/v1/getTags";
	public static String get_receipt = SERVER + "list/v1/get_receipt";
	public static String add_receipt = SERVER + "list/upload.php";

}
