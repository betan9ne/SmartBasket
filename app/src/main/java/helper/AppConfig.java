package helper;

public class AppConfig {
 	//local server
	public static String SERVER = "http://192.168.42.96/";
	//public static String SERVER = "http://192.168.8.100/";
	//public static String SERVER = "http://192.168.42.100/";
		//online server
//	private static String SERVER = "http://www.betan9ne.com/";

	public static String getLists = SERVER + "list/v1/getLists";
	public static String getPartners = SERVER + "list/v1/getPartners";
	public static String getBasketItems = SERVER + "list/v1/getBasketItems";
	public static String add_Item = SERVER + "list/v1/add_Item";
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


}
