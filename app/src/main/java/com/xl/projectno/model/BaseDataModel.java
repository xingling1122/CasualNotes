package com.xl.projectno.model;

import com.xl.projectno.data.protocol.ProtocolManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by huanglun on 17-1-6.
 */
public class BaseDataModel {
    public int mMsgId;
    public String mUpdateTime;
    public HashMap<String, ArrayList<BaseBean>> mDataMap = new HashMap<>();

    public ArrayList<BaseBean> getList(String key) {
        return mDataMap.get(key);
    }

    public boolean parseJSON(JSONObject jsonObject) throws JSONException {
        Iterator<?> iterator = jsonObject.keys();
        JSONArray moduleJsonArray = null;
        ArrayList<BaseBean> list = null;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            moduleJsonArray = jsonObject
                    .getJSONArray(key);
            list = parseData(key, moduleJsonArray);
            if (list != null) {
                mDataMap.put(key, list);
            }
        }

        return true;
    }

    public void parseHead() {

    }

    public static ArrayList<BaseBean> parseData(String key, JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }
        int arraySize = jsonArray.length();
        if (arraySize <= 0) {
            return null;
        }
        ArrayList<BaseBean> itemList = new ArrayList<>();
        JSONObject object = null;
        for (int i = 0; i < arraySize; i++) {
            object = jsonArray.getJSONObject(i);
            if (object != null) {
                BaseBean item = createBean(key);
                if(item != null) {
                    item.parseJson(object);
                    itemList.add(item);
                }
            }
        }

        return itemList;
    }

    public static BaseBean createBean(String key) {
        BaseBean bean = null;
        if (key.equalsIgnoreCase(ProtocolManager.LABEL_DATA_ITEMS)) {
            bean = new ItemBean();
        } else if (key.equalsIgnoreCase(ProtocolManager.LABEL_DATA_FOLDERS)) {
            bean = new FolderBean();
        } else if (key.equalsIgnoreCase(ProtocolManager.LABEL_DATA_INVENTORYS)) {
            bean = new InventoryBean();
        }
        return bean;
    }

}
