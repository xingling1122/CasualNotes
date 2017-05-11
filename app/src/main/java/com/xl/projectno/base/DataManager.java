package com.xl.projectno.base;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NoCache;
import com.xl.projectno.dao.FoldersOperator;
import com.xl.projectno.dao.InventorysOperator;
import com.xl.projectno.dao.ItemsOperator;
import com.xl.projectno.dao.ThemeOperator;
import com.xl.projectno.dao.UsersOperator;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.FormImage;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.ItemBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.volley.DataLoader;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.volley.PlayHttpClientStack;
import com.xl.projectno.volley.PostUploadRequest;
import com.xl.projectno.volley.RequestManager;
import com.xl.projectno.volley.ResponseListener;
import com.xl.projectno.volley.UrlCreateFactory;

import org.apache.http.client.params.HttpClientParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */
public class DataManager implements Handler.Callback{

    private ContentResolver mResolver = null;

    private DataLoader mLoader;

    private UserBean currentUser;

    /**
     * 网络线程池数量
     */
    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 2;

    //缓存
    private ArrayList<BaseBean> mItemBeans = new ArrayList<>();
    private ArrayList<BaseBean> mUserBeans = new ArrayList<>();
    private ArrayList<BaseBean> mInventoryBeans = new ArrayList<>();
    private ArrayList<BaseBean> mFolderBeans = new ArrayList<>();
    private ArrayList<BaseBean> mThemeBeans = new ArrayList<>();

    public static final int TYPE_OPERATE_ADD = 0x01;
    public static final int TYPE_OPERATE_DEL = 0x02;
    public static final int TYPE_OPERATE_UPDATE = 0x03;
    public static final int TYPE_OPERATE_MASK = 0x0F;

    public static final int TYPE_DATA_ITEM = 0x10;
    public static final int TYPE_DATA_USER = 0x20;
    public static final int TYPE_DATA_INVENTORY = 0x30;
    public static final int TYPE_DATA_FOLDER = 0x40;
    public static final int TYPE_DATA_THEME = 0x50;
    public static final int TYPE_DATA_MASK = 0xF0;

    private Handler mHandler = new Handler(this);

    private HashMap<Integer, ArrayList<IDataChangeListener>> mDataListener = new HashMap<>();

    private static DataManager mInstance;

    RequestQueue queue;

    private DataManager() {
        mResolver = MposApp.getApp().getContentResolver();
        RequestManager.init(MposApp.getApp().getApplicationContext());
        queue = newRequestQueue(MposApp.getApp().getApplicationContext());
        mLoader = new DataLoader(MposApp.getApp().getApplicationContext(), queue);
//        getAllData();
    }

    public synchronized static DataManager getInstance() {
        if(mInstance == null) {
            mInstance = new DataManager();
        }
        return mInstance;
    }

    //新建请求队列
    private RequestQueue newRequestQueue(Context context) {
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }

        AndroidHttpClient client = AndroidHttpClient.newInstance(userAgent);
        // 设置支持重定向
        HttpClientParams.setRedirecting(client.getParams(), true);
        PlayHttpClientStack stack = new PlayHttpClientStack(client);
        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new NoCache(), network,
                DEFAULT_NETWORK_THREAD_POOL_SIZE);
        queue.start();

        return queue;
    }

    public void updateCurrentUser(UserBean userBean){
        currentUser = userBean;
    }

    public UserBean getCurrentUser(){
        return currentUser;
    }

    @Override
    public boolean handleMessage(Message msg) {
        int dataType = msg.what & TYPE_DATA_MASK;
        int operateType = msg.what & TYPE_OPERATE_MASK;
        if(dataType == TYPE_DATA_ITEM) {
            if(operateType == TYPE_OPERATE_ADD) {
                if(msg.arg1 >= 0) {
                    ItemBean bean = (ItemBean) msg.obj;
                    mItemBeans.add(bean);
                }
            } else if(operateType == TYPE_OPERATE_DEL) {
                if(msg.arg1 >= 0) {
                    ItemBean bean = (ItemBean) msg.obj;
                    mItemBeans.remove(bean);
                }
            } else if (operateType ==TYPE_OPERATE_UPDATE){
                if (msg.arg1>=0) {
                    ItemBean bean = (ItemBean) msg.obj;
                    ArrayList<BaseBean> list = new ArrayList<>();
                    for (BaseBean itemBean:mItemBeans){
                        list.add(itemBean);
                    }
                    for (int i=0;i<list.size();i++){
                        ItemBean bean1 = (ItemBean)list.get(i);
                        if (bean.title.equals(bean1.title)){
                            mItemBeans.remove(i);
                            mItemBeans.add(i,bean);
                            break;
                        }
                    }
                }
            }
            notifyDataChange(TYPE_DATA_ITEM, msg.arg1);
        }else if(dataType == TYPE_DATA_INVENTORY) {
            if(operateType == TYPE_OPERATE_ADD) {
                if(msg.arg1 >= 0) {
                    InventoryBean bean = (InventoryBean) msg.obj;
                    mInventoryBeans.add(bean);
                }
            } else if(operateType == TYPE_OPERATE_DEL) {
                if(msg.arg1 >= 0) {
                    InventoryBean bean = (InventoryBean) msg.obj;
                    for (BaseBean baseBean:mInventoryBeans){
                        if (((InventoryBean)baseBean).name.equals(bean.name)){
                            mInventoryBeans.remove(baseBean);
                            break;
                        }
                    }
                }
            } else if (operateType ==TYPE_OPERATE_UPDATE){
                if (msg.arg1>=0) {
                    InventoryBean bean = (InventoryBean) msg.obj;
                    ArrayList<BaseBean> list = new ArrayList<>();
                    for (BaseBean itemBean:queryList(TYPE_DATA_INVENTORY)){
                        list.add(itemBean);
                    }
                    for (int i=0;i<list.size();i++){
                        InventoryBean bean1 = (InventoryBean)list.get(i);
                        if (bean.id==bean1.id){
                            mInventoryBeans.remove(i);
                            mInventoryBeans.add(i,bean);
                            break;
                        }
                    }
                }
            }
            notifyDataChange(TYPE_DATA_INVENTORY, msg.arg1);
        }else if(dataType == TYPE_DATA_FOLDER) {
            if(operateType == TYPE_OPERATE_ADD) {
                if(msg.arg1 >= 0) {
                    FolderBean bean = (FolderBean) msg.obj;
                    mFolderBeans.add(bean);
                }
            } else if(operateType == TYPE_OPERATE_DEL) {
                if(msg.arg1 >= 0) {
                    FolderBean bean = (FolderBean) msg.obj;
                    for (int i=0;i<mFolderBeans.size();i++){
                        FolderBean folderBean = (FolderBean) mFolderBeans.get(i);
                        if (bean.name.equals(folderBean.name)){
                            mFolderBeans.remove(i);
                        }
                    }
                }
            } else if (operateType ==TYPE_OPERATE_UPDATE){
                if (msg.arg1>=0) {
                    FolderBean bean = (FolderBean) msg.obj;
                    ArrayList<BaseBean> list = new ArrayList<>();
                    for (BaseBean itemBean:queryList(TYPE_DATA_FOLDER)){
                        list.add(itemBean);
                    }
                    for (int i=0;i<list.size();i++){
                        FolderBean bean1 = (FolderBean)list.get(i);
                        if (bean.id==bean1.id){
                            mFolderBeans.remove(i);
                            mFolderBeans.add(i,bean);
                            break;
                        }
                    }
                }
            }
            notifyDataChange(TYPE_DATA_FOLDER, msg.arg1);
        }else if(dataType == TYPE_DATA_THEME) {
            if(operateType == TYPE_OPERATE_ADD) {
                if(msg.arg1 >= 0) {
                    ThemeBean bean = (ThemeBean) msg.obj;
                    mThemeBeans.add(bean);
                }
            } else if(operateType == TYPE_OPERATE_DEL) {
                if(msg.arg1 >= 0) {
                    ThemeBean bean = (ThemeBean) msg.obj;
                    for (int i=0;i<mThemeBeans.size();i++){
                        ThemeBean themeBean = (ThemeBean) mThemeBeans.get(i);
                        if (bean.name.equals(themeBean.name)){
                            mThemeBeans.remove(i);
                        }
                    }
                }
            } else if (operateType ==TYPE_OPERATE_UPDATE){
                if (msg.arg1>=0) {
                    ThemeBean bean = (ThemeBean) msg.obj;
                    ArrayList<BaseBean> list = new ArrayList<>();
                    for (BaseBean itemBean:queryList(TYPE_DATA_THEME)){
                        list.add(itemBean);
                    }
                    for (int i=0;i<list.size();i++){
                        ThemeBean bean1 = (ThemeBean)list.get(i);
                        if (bean.id==bean1.id){
                            mThemeBeans.remove(i);
                            mThemeBeans.add(i,bean);
                            break;
                        }
                    }
                }
            }
            notifyDataChange(TYPE_DATA_THEME, msg.arg1);
        }
        return false;
    }

    //登录验证
    public boolean login(String usr, String psw,ILoadDataListener<UserBean> listener) {
        mLoader.login(usr, psw, TimeUtils.getNowTime(),listener);
        return true;
    }

    public boolean logout(String usr, String psw,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.logout(usr, psw, listener);
        return true;
    }

    public boolean keepLive(String usr, String api_token,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.keepLive(usr, api_token, listener);
        return true;
    }

    public boolean changeinfo(String usr, String api_token,String nickname,String phone,String imail,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.changeinfo(usr, api_token,nickname,phone,imail, listener);
        return true;
    }

    public boolean alterps(String usr, String oldpassword,String newpassword,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.alterps(usr, oldpassword,newpassword,listener);
        return true;
    }

    public boolean resetps(String imail, String phone,String checkcode,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.resetps(imail, phone,checkcode,listener);
        return true;
    }

    public boolean generatecode(String imail, String phone,ILoadDataListener<ResponseHeaderBean> listener) {
        mLoader.generatecode(imail, phone,listener);
        return true;
    }

    public boolean register(String usr,String psw,ILoadDataListener<UserBean> listener){
        mLoader.register(usr,psw,TimeUtils.getNowTime(),listener);
        return true;
    }

    public boolean uploadImage(String usr, String imagename, Bitmap bitmap, ResponseListener listener){
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(usr,imagename,bitmap)) ;
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_UPLOADIMAGE);
        Request request = new PostUploadRequest(url,imageList,listener) ;
        queue.add(request);
        return true;
    }

    public boolean setImage(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        Log.d("xingling","url:"+url);
        Request request = new ImageRequest(url,listener,0, 0, Bitmap.Config.RGB_565,errorListener) ;
        queue.add(request);
        return true;
    }

    private int getMaxSort(){
        return ItemsOperator.getInstance().getMaxSortN();
    }

    public ArrayList<BaseBean> getThemes(){
        return ThemeOperator.getInstance().getItems();
    }

    public boolean addItem(final BaseBean bean) {
        MessageHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
                int dataType = 0;
                int ret = -1;
                BaseBean baseBean1 =null;
                if(bean instanceof ItemBean) {
                    dataType = TYPE_DATA_ITEM;
                    ((ItemBean) bean).sortNo=getMaxSort()+1;
                    ret = ItemsOperator.getInstance().addItem((ItemBean) bean);
                    baseBean1 = ItemsOperator.getInstance().getBeanByCreateTime(((ItemBean)bean).createTime);
                }else if(bean instanceof UserBean) {
                    dataType = TYPE_DATA_USER;
                    ret = UsersOperator.getInstance().addItem((UserBean) bean);
                    baseBean1 = UsersOperator.getInstance().getBeanByName(((UserBean)bean).name);
                }else if(bean instanceof InventoryBean) {
                    if (((InventoryBean)bean).isUsing){
                        for (BaseBean baseBean:queryList(TYPE_DATA_INVENTORY)){
                            InventoryBean inventoryBean = (InventoryBean)baseBean;
                            inventoryBean.isUsing=false;
                            updateItem(inventoryBean,inventoryBean);
                        }
                    }
                    dataType = TYPE_DATA_INVENTORY;
                    ret = InventorysOperator.getInstance().addItem((InventoryBean) bean);
                    baseBean1 = InventorysOperator.getInstance().getBeanByCreateTime(((InventoryBean)bean).createTime);
                }else if(bean instanceof FolderBean) {
                    for (BaseBean baseBean:queryList(TYPE_DATA_FOLDER)){
                        FolderBean folderBean = (FolderBean)baseBean;
                        folderBean.isCheck=false;
                        updateItem(folderBean,folderBean);
                    }
                    dataType = TYPE_DATA_FOLDER;
                    ret = FoldersOperator.getInstance().addItem((FolderBean) bean);
                    baseBean1 = FoldersOperator.getInstance().getBeanByCreateTime(((FolderBean)bean).createTime);
                }else if(bean instanceof ThemeBean) {
                    dataType = TYPE_DATA_THEME;
                    ret = ThemeOperator.getInstance().addItem((ThemeBean) bean);
                    baseBean1 = ThemeOperator.getInstance().getTheme(((ThemeBean)bean).name);
                }
                Message msg = Message.obtain(mHandler, dataType|TYPE_OPERATE_ADD, ret,0, baseBean1);
                msg.sendToTarget();
            }
        });
        return true;
    }

    public boolean updateItem(final BaseBean bean, final BaseBean newBean) {
        MessageHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
                int dataType = 0;
                int ret = -1;
                if(bean instanceof ItemBean) {
                    dataType = TYPE_DATA_ITEM;
                    newBean.id=bean.id;
                    ret = ItemsOperator.getInstance().updateItem(((ItemBean) bean).title, (ItemBean) newBean);
                }else if(bean instanceof UserBean) {
                    dataType = TYPE_DATA_USER;
                    newBean.id=bean.id;
                    ret = UsersOperator.getInstance().updateItem(bean.id, (UserBean) newBean);
                }else if(bean instanceof InventoryBean) {
                    dataType = TYPE_DATA_INVENTORY;
                    newBean.id=bean.id;
                    ret = InventorysOperator.getInstance().updateItem(bean.id, (InventoryBean) newBean);
                }else if(bean instanceof FolderBean) {
                    dataType = TYPE_DATA_FOLDER;
                    newBean.id=bean.id;
                    ret = FoldersOperator.getInstance().updateItem(bean.id, (FolderBean) newBean);
                }else if(bean instanceof ThemeBean){
                    dataType = TYPE_DATA_THEME;
                    newBean.id=bean.id;
                    ret = ThemeOperator.getInstance().updateItem(bean.id, (ThemeBean) newBean);
                }
                Message msg = Message.obtain(mHandler, dataType|TYPE_OPERATE_UPDATE, ret,0, newBean);
                msg.sendToTarget();
            }
        });
        return true;
    }

    public boolean updaterItems(ArrayList<BaseBean> beans){
        for (BaseBean baseBean:beans){
            updateItemBd(baseBean,baseBean);
        }
        return true;
    }

    public boolean updateItemBd(final BaseBean bean, final BaseBean newBean) {
        MessageHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
                int dataType = 0;
                int ret = -1;
                if(bean instanceof ItemBean) {
                    dataType = TYPE_DATA_ITEM;
                    newBean.id=bean.id;
                    ret = ItemsOperator.getInstance().updateItem(((ItemBean) bean).title, (ItemBean) newBean);
                }else if(bean instanceof UserBean) {
                    dataType = TYPE_DATA_USER;
                    newBean.id=bean.id;
                    ret = UsersOperator.getInstance().updateItem(bean.id, (UserBean) newBean);
                }else if(bean instanceof InventoryBean) {
                    dataType = TYPE_DATA_INVENTORY;
                    newBean.id=bean.id;
                    ret = InventorysOperator.getInstance().updateItem(bean.id, (InventoryBean) newBean);
                }else if(bean instanceof FolderBean) {
                    dataType = TYPE_DATA_FOLDER;
                    newBean.id=bean.id;
                    ret = FoldersOperator.getInstance().updateItem(bean.id, (FolderBean) newBean);
                }
            }
        });
        return true;
    }

    public boolean delItem(final BaseBean bean) {
        MessageHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
                int dataType = 0;
                int ret = -1;
                if(bean instanceof ItemBean) {
                    dataType = TYPE_DATA_ITEM;
                    ret = ItemsOperator.getInstance().delItem((ItemBean) bean);
                }else if(bean instanceof UserBean) {
                    dataType = TYPE_DATA_USER;
                    ret = UsersOperator.getInstance().delItem((UserBean) bean);
                }else if(bean instanceof InventoryBean) {
                    dataType = TYPE_DATA_INVENTORY;
                    ret = InventorysOperator.getInstance().delItem((InventoryBean) bean);
                }else if(bean instanceof FolderBean) {
                    dataType = TYPE_DATA_FOLDER;
                    ret = FoldersOperator.getInstance().delItem((FolderBean) bean);
                }else if(bean instanceof ThemeBean) {
                    dataType = TYPE_DATA_THEME;
                    ret = ThemeOperator.getInstance().delItem((ThemeBean) bean);
                }
                Message msg = Message.obtain(mHandler, dataType|TYPE_OPERATE_DEL, ret, 0, bean);
                msg.sendToTarget();
            }
        });
        return true;
    }

    public ThemeBean getCurrentTheme(){
        return ThemeOperator.getInstance().getCurrentTheme();
    }

    public void delUser(){
        currentUser=null;
        UsersOperator.getInstance().delAllUsers();
    }

    public ArrayList<BaseBean> getListDirect(final  int dataType) {
        if(dataType == TYPE_DATA_ITEM) {
            return mItemBeans;
        }else if(dataType == TYPE_DATA_USER) {
            return mUserBeans;
        }else if(dataType == TYPE_DATA_INVENTORY) {
            return mInventoryBeans;
        }else if(dataType == TYPE_DATA_FOLDER) {
            return mFolderBeans;
        }else {
            return null;
        }
    }

    public ArrayList<BaseBean> queryList(final int dataType) {
        ArrayList<BaseBean> list = null;
        if(dataType == TYPE_DATA_ITEM) {
            list = ItemsOperator.getInstance().getItems();
        }else if(dataType == TYPE_DATA_INVENTORY) {
            list = InventorysOperator.getInstance().getItems();
        }else if(dataType == TYPE_DATA_FOLDER) {
            list = FoldersOperator.getInstance().getItems();
        }else if (dataType==TYPE_DATA_THEME) {
            list = ThemeOperator.getInstance().getItems();
        }else {
            list = null;
        }
        return list;
    }

    public UserBean getUser(){
        return UsersOperator.getInstance().getUser();
    }

    public ArrayList<BaseBean> getDropInventorys(){
        return InventorysOperator.getInstance().getItemsDrop();
    }

    public ArrayList<BaseBean> getInventoryBeansByFolderId(int folderId){
        return InventorysOperator.getInstance().getItemsByFolderId(folderId);
    }

    public ArrayList<BaseBean> getDropInventorysFM(){
        ArrayList<BaseBean> list = new ArrayList<>();
        for (BaseBean baseBean:mInventoryBeans){
            if (((InventoryBean)baseBean).isDrop){
                list.add(baseBean);
            }
        }
        return list;
    }

    public ArrayList<BaseBean> getInventoryItemsByFId(int folderId){
        ArrayList<BaseBean> list = new ArrayList<>();
        for (BaseBean baseBean:mInventoryBeans){
            InventoryBean inventoryBean = (InventoryBean)baseBean;
            if ((inventoryBean.folderId==folderId)&&(inventoryBean.isDrop==false)){
                list.add(baseBean);
            }
        }
        return list;
    }

    public ArrayList<BaseBean> queryOrderList(){
        return ItemsOperator.getInstance().getOrderItems();
    }

    public BaseBean getItemByName(String name,int dataType){
        switch (dataType){
            case TYPE_DATA_ITEM:
                return ItemsOperator.getInstance().getBeanByName(name);
            case TYPE_DATA_INVENTORY:
                return InventorysOperator.getInstance().getBeanByName(name);
            case TYPE_DATA_FOLDER:
                return FoldersOperator.getInstance().getBeanByName(name);
            case TYPE_DATA_USER:
                return UsersOperator.getInstance().getBeanByName(name);
        }
        return null;
    }

    public FolderBean getFolderBeanById(int id){
        return FoldersOperator.getInstance().getBeanById(id);
    }

    public boolean isExit(String oldname,String name,int dataType){
        switch (dataType){
            case TYPE_DATA_ITEM:
                if ((!name.equals(oldname))&&ItemsOperator.getInstance().isExit(name)){
                    return true;
                }else{
                    return false;
                }
            case TYPE_DATA_INVENTORY:
                if ((!name.equals(oldname))&&InventorysOperator.getInstance().isExit(name)){
                    return true;
                }else{
                    return false;
                }
            case TYPE_DATA_FOLDER:
                if ((!name.equals(oldname))&&FoldersOperator.getInstance().isExit(name)){
                    return true;
                }else{
                    return false;
                }
        }
        return false;
    }

    private void notifyDataChange(int msgId, int arg) {
        ArrayList<IDataChangeListener> list = mDataListener.get(msgId);
        if(list == null || list.isEmpty()) {
            return;
        }
        for(IDataChangeListener listener : list) {
            listener.onDataChange(msgId, arg);
        }
    }

    public void registerDataChangeListener(int dataType, IDataChangeListener listener) {
        ArrayList<IDataChangeListener> list = mDataListener.get(dataType);
        if(list == null || list.isEmpty()) {
            list = new ArrayList<>();
            mDataListener.put(dataType, list);
        }
        if(!list.contains(listener)) {
            list.add(listener);
        }
    }

    public void unregisterDataChange(int dataType, IDataChangeListener listener) {
        ArrayList<IDataChangeListener> list = mDataListener.get(dataType);
        if(list == null || list.isEmpty()) {
            return;
        }
        if(list.contains(listener)) {
            list.remove(listener);
        }
    }

    public void updateListFromDb(){
        mItemBeans = queryOrderList();
        mUserBeans = queryList(TYPE_DATA_USER);
        mInventoryBeans=queryList(TYPE_DATA_INVENTORY);
        mFolderBeans=queryList(TYPE_DATA_FOLDER);
        mThemeBeans =queryList(TYPE_DATA_THEME);
    }

    public interface IDataChangeListener {
        public void onDataChange(int msgId, int arg);
    }
}
