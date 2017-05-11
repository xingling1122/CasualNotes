// IKeepLiveInterface.aidl
package com.xl.projectno;

// Declare any non-default types here with import statements

interface IKeepLiveInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int keepLive();
    void removeCallBack();
    void sendCallBack();
}
