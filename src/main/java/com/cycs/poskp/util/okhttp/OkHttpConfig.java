/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.util.okhttp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * OkHttp 配置类
 *
 * @author L.Y
 */
@Configuration
public class OkHttpConfig {
	private static final Logger log = LoggerFactory.getLogger(OkHttpConfig.class);
    private static final String TLS = "TLS";

    @Value("${http.ok-http.max-idle-connections:200}")
    private int maxIdleConnections;

    @Value("${http.ok-http.keep-alive-duration:300}")
    private int keepAliveDuration;
    /**
     * Sets the default connect timeout for new connections
     */
    @Value("${http.ok-http.connect-timeout:10000}")
    private long connectTimeout;
    /**
     * Sets the default read timeout for new connections
     */
    @Value("${http.ok-http.read-timeout:5000}")
    private long readTimeout;
    /**
     * Sets the default write timeout for new connections.
     */
    @Value("${http.ok-http.write-timeout:5000}")
    private long writeTimeout;
    /**
     * Sets the default timeout for complete calls
     */
    @Value("${http.ok-http.call-timeout:30000}")
    private long callTimeout;

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory(X509TrustManager x509TrustManager) {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance(TLS);
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * this pool holds up to {@code maxIdleConnections} idle connections
     * which will be evicted after {@code keepAliveDuration} minutes of inactivity.
     */
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS);
    }

    /**
     * 构建okhttp实例
     * @return
     */
    @Bean
    public OkHttpClient okHttpClient(SSLSocketFactory sslSocketFactory,
            X509TrustManager x509TrustManager,
            ConnectionPool pool) {
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .retryOnConnectionFailure(true)//是否重试
                .connectionPool(pool())//连接池
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                //.callTimeout(callTimeout, TimeUnit.SECONDS)
                /*.addNetworkInterceptor(new Interceptor() {
					
                	@Override
                	public Response intercept(Chain chain)throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Connection", "close").build();
                        return chain.proceed(request);
                    }

					
				})*/
                .build();
    }
}
