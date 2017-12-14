package com.marco.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.marco.demo.R;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit 参考：http://www.jianshu.com/p/0f97f94b171f
 * okhttp官方文档：https://github.com/square/okhttp/wiki
 */

public class RetrofitExampleActivity extends Activity {

    private static final String TAG = "RetrofitExampleActivity";
    private static Map<String, Object> sMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_example);

        findViewById(R.id.btn_retrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 设置拦截器-注意：日志拦截器中无法获取之后在拦截器添加的header信息
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(new LoggingInterceptor())
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .client(httpClient)
                        .build();

//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://api.github.com/")
//                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<ResponseBody> repos = service.listRepos("octocat", "yb");
                repos.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.i(TAG, "onResponse : " + response.body().string());
                            response.body().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "onFailure");
                    }
                });
            }
        });
    }

    public interface GitHubService {
        @GET("users/{user}/repos")
        Call<ResponseBody> listRepos(@Path("user") String user, @Query("name") String name);
    }

    class HeaderInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Log.i(TAG, "HeaderInterceptor -> intercept");

            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();
            builder.header("appid", "1");
            builder.header("timestamp", System.currentTimeMillis() + "");
            builder.header("appkey", "zRc9bBpQvZYmpqkwOo");
            builder.header("signature", "dsljdljflajsnxdsd");

            Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Log.i(TAG, "LoggingInterceptor -> intercept");

            final Request request = chain.request();

            long t1 = System.nanoTime();
            // 备注：拦截后的url是带query参数的
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.text_info)).setText(request.url().toString());
                }
            });
            Log.i(TAG, String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            okhttp3.Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
