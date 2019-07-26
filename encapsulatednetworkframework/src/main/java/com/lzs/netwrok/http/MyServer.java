package com.lzs.netwrok.http;




import com.lzs.netwrok.bean.BaseResponse;
import com.lzs.netwrok.bean.ListData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface MyServer {
    @GET
    Observable<BaseResponse<List<ListData>>> get(@Url String url);
}
