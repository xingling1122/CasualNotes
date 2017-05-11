package com.xl.projectno.volley;

import android.os.Environment;

import com.xl.projectno.utils.LogUtil;

/**
 * 
 * <br>类描述:公共变量定义
 * <br>功能详细描述:
 */
public interface GoPluginEnv {
	
	/** 
	 * 定义是否发布版本，未经允许不要修改提交（警告！）
	 */
	public final static boolean DEBUG = !LogUtil.sEnable;
	
	/**
	 * 插件商店
	 */
	public final static String TAG = "GoPluginStore";
	
	/**
	 * 插件商店的根节点的虚拟id
	 */
	public final static int GO_PLUGIN_STORE_ROOT_VIRTUAL_ID = 100;
	/**
	 * 主题商店的根节点的虚拟id
	 */
	public final static int GO_STORE_STORE_ROOT_VIRTUAL_ID = 101;
	/**
	 * 按键音商店的根节点的虚拟id
	 */
	public final static int GO_KEYTONE_ROOT_VIRTUAL_ID = 103;
	
	/**
	 * 快捷主题切换根节点的虚拟id 
	 */
	public final static int GO_STORE_QUICK_CHANGE_THEME = 206;
	
	/**
	 * 正式服务器,字体商店的根节点的虚拟id
	 */
	public final static int GO_STORE_FONT_ROOT_VIRTUAL_ID = 208;
	/**
	 * 测试服务器,字体商店的根节点的虚拟id
	 */
	public final static int GO_STORE_FONT_ROOT_VIRTUAL_ID_TEST = 108;
	
	/**
	 * 正式服务器,贴图推荐的根节点的虚拟id
	 */
	public final static int FACE_STICKER_ROOT_VIRTUAL_ID = 231;
	/**
	 * 测试服务器,贴图推荐的根节点的虚拟id
	 */
	public final static int FACE_STICKER_ROOT_VIRTUAL_ID_TEST = 219;
	
	/**
	 * 语言zip包请求的根节点的虚拟id
	 */
	public final static int LANGUAGE_ZIP_ROOT_VIRTUAL_ID_TEST = 210;
	
	/**
	 * 贴图表情请求的根节点的虚拟id
	 */
	public final static int STICKER_ROOT_VIRTUAL_ID = 222; // 正式服
	public final static int STICKER_ROOT_VIRTUAL_TEST_ID = 218; // 测试服
	
	/**
	 * 进入插件商店入口
	 */
	public final static int ENTRANCE_ENTER_GO_PLUGIN_STORE = 1;
	
	/**
	 * 插件商店local入口
	 */
	public final static int ENTRANCE_ENTER_GO_PLUGIN_STORE_LOCAL = 2;
	
	
	public static final String KEY_OTHER_THEMES_OF_THE_SUIT = "other_themes_of_the_suit";
	public static final String KEY_APPDETAIL_BY_MAPID = "appinfo_detail";
	
	/**
	 * 路径类 所有路径相关的常量都统一放在此处
	 * @author luopeihuan
	 * @add by zhuotao
	 */
	public static final class Path {

		/**
		 * sdcard head
		 */
		public final static String SDCARD = Environment.getExternalStorageDirectory().getPath()
				+ "/gokeyboard/goplugin";
		
		public final static String DATABASE_SDCARD = Environment.getExternalStorageDirectory().getPath()
				+ "/Android/data/gokeyboard";

		/**
		 * 内置储存控件
		 */
		public final static String PACKAGE_FILE_PATH = "/data/data/" + "mpos" + "/files";

		/**
		 * 存放异常数据
		 */
		public static final String KITTY_EXCEPTION_PATH = SDCARD + "/info/exception/";

//		/**
//		 * 存放协议模型的数据
//		 */
//		public static final String LAUNCHER_DIR = SDCARD + "/cache/dir/";

		/**
		 * 图片路径
		 */
		public static final String IMAGES_DIR = SDCARD + "/cache/image/";

		/**
		 * 按键音路径
		 */
		public static final String KEYTONES_DIR = PACKAGE_FILE_PATH + "/data/";
		
		/**
		 * 语言包sd存放路径
		 */
		public static final String LANGUAGE_DIR = SDCARD + "/language/";
		
		/**
		 * 语言包sd下载路径
		 */
		public static final String DOWN_LANGUAGE_DIR = SDCARD + "/download/";
		
		/**
		 *语言包内存路径
		 */
		public static final String LANGUAGE_CACHE_DIR = PACKAGE_FILE_PATH + "/language/";
		
		/**
		 * 分享图片sd路径
		 */
		public static final String SHARE_PICTURE_DIR = SDCARD + "/share/";
		
		/**
		 *分享图片内存路径
		 */
		public static final String SHARE_PICTURE_CACHE_DIR = PACKAGE_FILE_PATH + "/share/";

		/**
		 * 保存请求数据的路径（调试）
		 */
		public static final String DEBUG_QUEST_DATA_PATH = SDCARD + "/home.txt";
	}
	
	public static final class Server {
		public static final String HTTP = "http://";
		
		/**
		 * 域名，正式服务器
		 */
		public static final String HOST = "http://123.207.244.252:8080/CasualNotes/api/";
		
		/**
		 * 域名,测试服务器
		 */
		public static final String HOST_TEST = "http://192.168.43.112:8080/CasualNotes/api/";
		
		/**
		 * 1.插件商店获取各模块的数据(支持批量获取)
		 */
		public static final String GET_Plugin_MODULE_DATA_URL = "/gokeyboard_market/common?funid=1&rd=";
		/**
		 * 2.主题获取各模块的数据(支持批量获取)
		 */
		public static final String GET_STORE_MODULE_DATA_URL = "/gokeyboard_market/common?funid=6&rd=";
		/**
		 * 3.通过Mapid获取主题数据
		 */
		public static final String GET_STORE_DETAIL_MAPID_DATA_URL = "/gokeyboard_market/common?funid=8&rd=";
		/**
		 * 4.猜你喜欢主题获取各模块的数据
		 */
		public static final String GET_STORE_GUESS_U_LIKE_DATA_URL = "/gokeyboard_market/common?funid=7&rd=";
		
		/**
		 * 5.按键音商店获取各模块的数据
		 */
		public static final String GET_KEYTONE_DATA_URL = "/gokeyboard_market/common?funid=11&rd=";
		
		/**
		 * 语言包更新的url
		 */
		public static final String GET_CHECK_LANGUAGE_DATA_URL = "/gokeyboard_market/common?funid=10&rd=";
	}
	
	/**
	 * 类描述: SharedPreferences的相关常量定义
	 *
	 * @author  dengxiaoming
	 * @date  [2014年1月8日]
	 */
	public final class SharedPreferencesEnv {
		/**
		 * 保存头信息相关内容的文件名称
		 */
		public static final String GOPLUGIN_REQUEST_INFO = "goplugin_request_info";
		
		/**
		 * 检查本地图片缓存的时间
		 */
		public static final String IMAGE_CHCHE_TIME_ID = "image_cache_time_id";
		/**
		 * 设备ID
		 */
		public static final String DEVICE_ID = "device_id";
		
		/**
		 * 保存头信息相关内容的文件名称
		 */
		public static final String REQUEST_INFO = "request_info";
		/**
		 * 渠道ID
		 */
		public static final String CHANNEL_ID = "channel_id";
	}
	
	/**
	 * Market搜索用到的常量
	 * 
	 * @author luopeihuan
	 */
	public static final class Market {
		public static final String PACKAGE = "com.android.vending";

		// 用包名搜索market上的软件
		public static final String BY_PKGNAME = "market://search?q=pname:";

		// 直接使用关键字搜索market上的软件
		public static final String BY_KEYWORD = "market://search?q=";

		// 进入软件详细页面
		public static final String APP_DETAIL = "market://details?id=";

		// 浏览器版本的电子市场详情地址
		public static final String BROWSER_APP_DETAIL = "https://play.google.com/store/apps/details?id=";
	}
}
