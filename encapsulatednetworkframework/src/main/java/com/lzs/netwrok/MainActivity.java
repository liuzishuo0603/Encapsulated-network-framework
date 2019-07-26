package com.lzs.netwrok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzs.netwrok.adapter.MainAdapter;
import com.lzs.netwrok.bean.BaseResponse;
import com.lzs.netwrok.bean.ListData;
import com.lzs.netwrok.http.HttpManager;
import com.lzs.netwrok.http.MyServer;
import com.lzs.netwrok.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRlv;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        HttpManager.getInstance().getServer(MyServer.class).get("wxarticle/chapters/json")
                .compose(RxUtils.<BaseResponse<List<ListData>>>rxSwitchObservableTransformer())
                .compose(RxUtils.<List<ListData>>rxConversionObservableTransform())
                .subscribe(new Consumer<List<ListData>>() {
                    @Override
                    public void accept(List<ListData> listData) throws Exception {
                        mAdapter.setData(listData);
                    }
                });
    }

    private void initView() {
        mRlv = (RecyclerView) findViewById(R.id.rlv);
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mRlv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new MainAdapter(this);
        mRlv.setAdapter(mAdapter);
    }
}
