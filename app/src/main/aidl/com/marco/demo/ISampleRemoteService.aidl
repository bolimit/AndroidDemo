// ISampleRemoteService.aidl
package com.marco.demo;
import com.marco.demo.ISampleRemoteServiceCallback;
import com.marco.demo.model.Config;

// Declare any non-default types here with import statements

interface ISampleRemoteService {
    int getStateInSeparateProcessAction();

    oneway void registerCallback(in ISampleRemoteServiceCallback callback);

    oneway void unregisterCallback(in ISampleRemoteServiceCallback callback);

    oneway void callInSeparateProcessAction(in Config config);
}
