package com.hackensack.umc.listener;

import epic.mychart.android.library.open.WPLoginResultType;

/**
 * Created by bhagyashree_kumawat on 1/28/2016.
 */
public interface ILoginInteface {
    void didLogIn(boolean islogin);
    void showNewTems(WPLoginResultType result);
}

