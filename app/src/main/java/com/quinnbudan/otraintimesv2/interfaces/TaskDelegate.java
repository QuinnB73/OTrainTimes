package com.quinnbudan.otraintimesv2.interfaces;

import java.util.ArrayList;

/**
 * Created by quinnbudan on 2016-12-31.
 */
public interface TaskDelegate {
    /*
        Notifies an activity of an async method's completion
     */
    public void taskCompletionResult(ArrayList<String> result);
}
