package com.xl.projectno.volley;

import com.android.volley.Response;

/**
 * Created by gyzhong on 15/3/1.
 */
public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}
