package com.web.common.utils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class WebConnect {
	 private HttpClient webClient;
	 
	 private Log log = LogFactory.getLog(getClass());
	 
	 
	public HttpClient getWebClient() {
		return webClient;
	}
	public void setWebClient(HttpClient webClient) {
		this.webClient = webClient;
	}
	public void initWebClient(String proxyHost, int proxyPort){
        this.initWebClient();
        if(webClient != null && !StringUtils.isEmpty(proxyHost)){
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            webClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
	}
	/**
     * @desc 初始化创建 WebClient
     */
    public void initWebClient() {
        log.info("initWebClient start....");
        try {
            PoolingClientConnectionManager tcm = new PoolingClientConnectionManager();
            tcm.setMaxTotal(400);
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
 
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
 
                }
 
                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
 
                }
 
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new X509TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, ssf);
            tcm.getSchemeRegistry().register(sch);
            webClient = new DefaultHttpClient(tcm);
	        } catch (Exception ex) {
	            log.error("initWebClient exception", ex);
	        } finally {
	            log.info("initWebClient end....");
	        }
        }
    /**
     * @desc 发起HTTP GET请求返回数据
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String executeHttpGet(String url) throws IOException, ClientProtocolException {
        ResponseHandler<?> responseHandler = new BasicResponseHandler();
        String response = (String) this.webClient.execute(new HttpGet(url), responseHandler);
        log.info("return response=====start======");
        log.info(response);
        log.info("return response=====end======");
        return response;
    }
}
