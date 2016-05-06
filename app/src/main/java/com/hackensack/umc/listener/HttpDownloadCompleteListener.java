package com.hackensack.umc.listener;

import com.hackensack.umc.datastructure.HttpResponse;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public interface HttpDownloadCompleteListener {


     void HttpDownloadCompleted(HttpResponse data);

}
