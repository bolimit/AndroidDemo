// ISampleRemoteServiceCallback.aidl
package com.marco.demo;

// Declare any non-default types here with import statements

oneway interface ISampleRemoteServiceCallback {
    void onCallbackInUIProcess(int value);
}
