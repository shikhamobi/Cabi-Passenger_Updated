package com.cabipassenger.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cabipassenger.R;
import com.cabipassenger.features.CToast;
import com.cabipassenger.util.SessionSave;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import co.example.developer.myndklibrary.AA;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by developer on 8/31/16.
 * Initiate the Service generator class for Retrofit service
 * This class to be initiated before each api is calling
 */
public class ServiceGenerator {
    private final OkHttpClient.Builder httpClient;
    private final Retrofit.Builder builder;


    public ServiceGenerator(Context c, boolean dont_encode) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);

        if (!dont_encode)
            httpClient.addInterceptor(d);
        httpClient.addInterceptor(b);


        builder =
                new Retrofit.Builder()
                        .baseUrl(SessionSave.getSession("base_url", c))
                        .addConverterFactory(GsonConverterFactory.create());
    }

    public ServiceGenerator(Context c, boolean dont_encode, int timeout) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        httpClient = new OkHttpClient.Builder().connectTimeout(timeout == 0 ? 30 : timeout, TimeUnit.SECONDS).readTimeout(timeout == 0 ? 30 : timeout, TimeUnit.SECONDS);

        /*commented by padma url log*/

        httpClient.interceptors().add(logging);

        if (!dont_encode)
            httpClient.addInterceptor(d);
        httpClient.addInterceptor(b);


        builder =
                new Retrofit.Builder()
                        .baseUrl(SessionSave.getSession("base_url", c))
                        .addConverterFactory(GsonConverterFactory.create());
    }

    public ServiceGenerator(Context c) {
        // http://192.168.1.115:1021/mobileapi115/supervisor_assignedjobs/dGF4aV9hbGw=/
        //  final String API_BASE_URL = "http://192.168.1.115:1021/mobileapi115/";
        //  final String API_BASE_URL = "http://192.168.1.65:1007/mobileapi115/";
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        //to encode api to base64
        // httpClient.interceptors().add(b);
        httpClient.addInterceptor(d);
        httpClient.addInterceptor(b);


        builder =
                new Retrofit.Builder()
                        .baseUrl(SessionSave.getSession("base_url", c))
                        .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * this method is used to convert url to base 64 and handle the time out errors
     */

    public ServiceGenerator(Context c, String Url, boolean dont_encode) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //set your desired log level
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        httpClient.addInterceptor(b);
        if (dont_encode)
            httpClient.addInterceptor(d);
        builder =
                new Retrofit.Builder()
                        .baseUrl(Url)
                        .addConverterFactory(GsonConverterFactory.create());
//
    }


    public ServiceGenerator(Context c, String Url, boolean dont_encode, int timeout) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //set your desired log level
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(timeout == 0 ? 30 : timeout, TimeUnit.SECONDS).readTimeout(timeout == 0 ? 30 : timeout, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        httpClient.addInterceptor(b);
        if (!dont_encode)
            httpClient.addInterceptor(d);
        builder =
                new Retrofit.Builder()
                        .baseUrl(Url)
                        .addConverterFactory(GsonConverterFactory.create());
//
    }

    public <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


    public class DecryptedPayloadInterceptor implements Interceptor {


        Context c;

        public DecryptedPayloadInterceptor(Context c) {
            this.c = c;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = null;
            response = chain.proceed(chain.request());

            if (response.isSuccessful()) {
                Response.Builder newResponse = response.newBuilder();
                String contentType = response.header("Content-Type");
                if (TextUtils.isEmpty(contentType)) contentType = "application/json";
                InputStream cryptedStream = response.body().byteStream();
                String decrypted = null;
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = cryptedStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                try {
                    if (!result.toString("UTF-8").isEmpty())

                        decrypted = new AA().dd(result.toString("UTF-8"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (decrypted == null || decrypted.trim().isEmpty()) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            CToast.ShowToast(c, c.getString(R.string.server_con_error));
                        }
                    });
                }
                try {
                    newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Response ress = newResponse.build();
                System.out.println("!!!!*******ddd" + decrypted);
                return ress;
            }
            System.out.println("!!!!*******" + response.body());
            return response;
        }
    }


    /**
     * this class converts the url to base 64
     */

    public class Base64EncodeRequestInterceptor implements Interceptor {


          //String companyKey = "1qfv2Nu6m8JTClxVzn/h4A==";  // live client

        // String companyKey = "fjGVOpcwGLtO5sNYCM2lr9OgfantouzfgQT7e1u3H0A=";   //local server
         String companyKey = "hTskNtAjkJswl5lxZ1ipYuFIKwUbI3Bt68fioD5/DrQ=";   //development server

        Base64EncodeRequestInterceptor(String key) {
            if (!key.trim().isEmpty())
                companyKey = key;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();
            System.out.println("*******" + originalRequest.method());
            if (originalRequest.method().equalsIgnoreCase("POST")) {
                builder = originalRequest.newBuilder()
                        .method(originalRequest.method(), encode(originalRequest.body()));

                System.out.println("*******" + originalRequest.method() + "____" + originalRequest.body());
            }
            builder.addHeader("Authorization", companyKey);


            return chain.proceed(builder.build());
        }

        private RequestBody encode(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);

                    String to_encode = new String(buffer.readByteArray(), "UTF-8");
                    String encodedString = "";
                    try {

                        encodedString = new AA().ee(to_encode);
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                    
                    System.out.println("encrypted__*" + encodedString);
                    System.out.println("before_encrypt" + to_encode);
                    sink.write(encodedString.getBytes("ISO-8859-1"));
                    buffer.close();
                    sink.close();
                }
            };
        }
    }

}


//public class ServiceGenerator {
//    private final OkHttpClient.Builder httpClient;
//    private final Retrofit.Builder builder;
//
//
//
//
//
//    public ServiceGenerator(Context c, boolean dont_encode) {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
//
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//
////        if (!dont_encode)
////            httpClient.addInterceptor(d);
//        httpClient.addInterceptor(b);
//
//
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(SessionSave.getSession("base_url", c))
//                        .addConverterFactory(GsonConverterFactory.create());
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//    public ServiceGenerator(Context c, boolean dont_encode,int timeout) {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
//
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        httpClient = new OkHttpClient.Builder().connectTimeout(timeout==0?30:timeout, TimeUnit.SECONDS).readTimeout(timeout==0?30:timeout, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//
////        if (!dont_encode)
////            httpClient.addInterceptor(d);
//        httpClient.addInterceptor(b);
//
//
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(SessionSave.getSession("base_url", c))
//                        .addConverterFactory(GsonConverterFactory.create());
//    }
//
//    public ServiceGenerator(Context c) {
//        // http://192.168.1.115:1021/mobileapi115/supervisor_assignedjobs/dGF4aV9hbGw=/
//        //  final String API_BASE_URL = "http://192.168.1.115:1021/mobileapi115/";
//        //  final String API_BASE_URL = "http://192.168.1.65:1007/mobileapi115/";
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
//
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        //to encode api to base64
//        // httpClient.interceptors().add(b);
//      //  httpClient.addInterceptor(d);
//        httpClient.addInterceptor(b);
//
//
//
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(SessionSave.getSession("base_url", c))
//                        .addConverterFactory(GsonConverterFactory.create());
//    }
//
//    /**
//     * this method is used to convert url to base 64 and handle the time out errors
//     */
//
//    public ServiceGenerator(Context c, String Url, boolean dont_encode) {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        //set your desired log level
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        httpClient.addInterceptor(b);
////        if (!dont_encode)
////            httpClient.addInterceptor(d);
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(Url)
//                        .addConverterFactory(GsonConverterFactory.create());
////
//    }
//
//
//    public ServiceGenerator(Context c, String Url, boolean dont_encode,int timeout) {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        //set your desired log level
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c));
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().connectTimeout(timeout==0?30:timeout, TimeUnit.SECONDS).readTimeout(timeout==0?30:timeout, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        httpClient.addInterceptor(b);
////        if (!dont_encode)
////            httpClient.addInterceptor(d);
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(Url)
//                        .addConverterFactory(GsonConverterFactory.create());
////
//    }
//
//    public <S> S createService(Class<S> serviceClass) {
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//        return retrofit.create(serviceClass);
//    }
//
//
//    public class DecryptedPayloadInterceptor implements Interceptor {
//
//
//        //        public interface DecryptionStrategy {
////            String decrypt(InputStream stream);
////        }
//        Context c;
//
//        public DecryptedPayloadInterceptor(Context c) {
//            this.c = c;
//        }
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Response response = null;
//            response = chain.proceed(chain.request());
//
//            if (response.isSuccessful()) {
//                Response.Builder newResponse = response.newBuilder();
//                String contentType = response.header("Content-Type");
//                if (TextUtils.isEmpty(contentType)) contentType = "application/json";
//                InputStream cryptedStream = response.body().byteStream();
//                String decrypted = null;
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = cryptedStream.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                }
//                ////System.out.println("!!!!*******__%%" + result.toString("UTF-8"));
//                try {
//                    if (!result.toString("UTF-8").isEmpty())
//
//                        decrypted = new AA().dd(result.toString("UTF-8"));
//
//
//                  //  ////System.out.println("decrypted_text:"+new AA().dd("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"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (decrypted == null || decrypted.trim().isEmpty()) {
//
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            CToast.ShowToast(c, c.getString(R.string.server_con_error));
//                        }
//                    });
//                }
//                try {
//                    newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Response ress = newResponse.build();
//                ////System.out.println("!!!!*******ddd" + decrypted);
//                return ress;
//            }
//            ////System.out.println("!!!!*******" + response.body());
//
//
//            return response;
//        }
//    }
//
//
//    /**
//     * this class converts the url to base 64
//     */
//
//    public class Base64EncodeRequestInterceptor implements Interceptor {
//        String companyKey = "FNpfuspyEAzhjfoh2ONpWK0rsnClVL6OCaasqDQtWdI=";
//
//        Base64EncodeRequestInterceptor(String key) {
//            if (!key.trim().isEmpty())
//                companyKey = key;
//        }
//
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request originalRequest = chain.request();
//            Request.Builder builder = originalRequest.newBuilder();
//            ////System.out.println("*******" + originalRequest.method());
//            if (originalRequest.method().equalsIgnoreCase("POST")) {
//                builder = originalRequest.newBuilder()
//                        .method(originalRequest.method(), (originalRequest.body()));
//
//                ////System.out.println("*******" + originalRequest.method() + "____" + originalRequest.body());
//            }
//            builder.addHeader("Authorization", companyKey);
//
//
//            return chain.proceed(builder.build());
//        }
//
////        private RequestBody encode(final RequestBody body) {
////            return new RequestBody() {
////                @Override
////                public MediaType contentType() {
////                    return body.contentType();
////                }
////
////                @Override
////                public void writeTo(BufferedSink sink) throws IOException {
////                    Buffer buffer = new Buffer();
////                    body.writeTo(buffer);
////
////                    String to_encode = new String(buffer.readByteArray(), "UTF-8");
////                    String encodedString = "";
////                    try {
////
////                        encodedString = new AA().ee(to_encode);
////                    } catch (GeneralSecurityException e) {
////                        e.printStackTrace();
////                    }
////                    ////System.out.println("encrypted_te" + encodedString);
////                    ////System.out.println("before_encrypt" + to_encode);
////                    sink.write(encodedString.getBytes("ISO-8859-1"));
////                    buffer.close();
////                    sink.close();
////                }
////            };
////        }
//    }
//
//}