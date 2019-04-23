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


}
