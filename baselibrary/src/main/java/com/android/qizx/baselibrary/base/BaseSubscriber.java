package com.android.qizx.baselibrary.base;



import android.content.Context;

import com.android.qizx.baselibrary.util.LogUtil;
import com.android.qizx.baselibrary.util.NetConnectManager;
import com.android.qizx.baselibrary.util.ToastUtil;
import com.android.qizx.baselibrary.widget.LoadingLayout;


/**
 * Created by 87901 on 2016/5/5.
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;
    private LoadingLayout loadingLayout;


    public BaseSubscriber(Context context,LoadingLayout loadingLayout){
        this.context=context;
        this.loadingLayout=loadingLayout;
    }

    @Override
    public void onStart() {
        if (!NetConnectManager.isNetworkAvailable(context)){
            ToastUtil.showToast(context,"亲，网络好像出问题啦");
            if (loadingLayout!=null){

                loadingLayout.setStatus(LoadingLayout.No_Network);
                unsubscribe();
            }

        }
    }

    @Override
    public void onNext(T t) {
        if (loadingLayout!=null){

            loadingLayout.setStatus(LoadingLayout.Success);
        }


    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        if (loadingLayout!=null){

            loadingLayout.setStatus(LoadingLayout.Error);
        }
        LogUtil.d("onError  "+throwable.getMessage());

        /*if(throwable instanceof HttpException){
            HttpException httpException= (HttpException) throwable;
            try {
                String errorBody= httpException.response().errorBody().string();

                LogUtil.d("onError  errorBody  "+errorBody);

                ErrorBean errorBean = new Gson().fromJson(errorBody, ErrorBean.class);
                if (errorBean!=null){
                    if (errorBean.getStatus_code()==401){
                        ToastUtil.showToast("账号已过期，请重新登录");
                        PreferencesHelper preferencesHelper = new PreferencesHelper(GlobalApplication.getContext());
                        preferencesHelper.Clear();
                        EventBusUtil.post(new LoginEvent());
                    }else if (errorBean.getStatus_code()==468){
                        ToastUtil.showToast("您输入的文字涉及敏感词，建议修改");
                    }else {
                        ToastUtil.showToast("亲，访问好像出问题了");
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

}
