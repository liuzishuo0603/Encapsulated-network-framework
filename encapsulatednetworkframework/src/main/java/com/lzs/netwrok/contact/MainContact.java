package com.lzs.netwrok.contact;


import com.lzs.netwrok.bean.ListData;

import java.util.List;

public interface MainContact {
    interface MainView {
        void onSuccessful(List<ListData> listData);

        void onFailed(String error);
    }

    interface MainPresenter {
        void setData();
    }

    interface MainModel {
        interface MainCallBack {
            void onSuccessful(List<ListData> listData);

            void onFailed(String error);
        }

        void getData(MainCallBack callBack);
    }
}
