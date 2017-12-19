package com.bs.frags;


import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.R;
import com.show.api.ShowApiRequest;

import java.util.Date;

/**
 * 直播界面，用于对接直播功能
 *
 * @author
 * @date 2017-5-3
 */
public class LiveFrag extends BaseFrag {

    @Override
    public int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.frag_live;
    }

    //以下代码为纯java实现，并未依赖第三方框架,具体传入参数请参看接口描述详情页.
    protected Handler mHandler = new Handler();
    TextView txt;
    @Override
    public void initView() {
        txt = view.findViewById(R.id.textView1);
        txt.setOnClickListener(this);

        new Thread() {
            //在新线程中发送网络请求
            @Override
            public void run() {
                String appid = "52480";//要替换成自己的
                String secret = "67b9ecfa346743e4a44d1b8fabe21098";//要替换成自己的
                final String res = new ShowApiRequest("http://route.showapi.com/6-1", appid, secret)
                        .addTextPara("num","189123456789")
                        .post();

                System.out.println(res);
                //把返回内容通过handler对象更新到界面
                mHandler.post(new Thread() {
                    @Override
                    public void run() {
                        txt.setText(res + "  " + new Date());
                    }
                });
            }
        }.start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView1:

            break;
        }
    }
}