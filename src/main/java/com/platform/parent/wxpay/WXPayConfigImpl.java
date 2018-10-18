package com.platform.parent.wxpay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WXPayConfigImpl extends WXPayConfig{
    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;

    private WXPayConfigImpl() throws Exception{
        // billy commented this for now, no need to use cert.
//        String certPath = "C:/dengb/personal/work"; //"D://CERT/common/apiclient_cert.p12";
//        File file = new File("apiclient_cert.p12");
//        InputStream certStream = new FileInputStream(file);
        InputStream certStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("apiclient_cert.p12");
//        Thread.currentThread().getContextClassLoader().getResourceAsStream()

        this.certData = input2byte(certStream);
    }

    public static WXPayConfigImpl getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static final byte[] input2byte(InputStream inStream)
    {
        if (inStream == null)
        {
            return null;
        }

        byte[] in2b = null;
        try
        {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in2b = swapStream.toByteArray();
        }
        catch (IOException e)
        {
            try
            {
                inStream.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

            e.printStackTrace();
        }

        return in2b;
    }

    public String getAppID() {
        return "wxca775a8dbdc1b4d5";
    }

    public String getMchID() {
        return "1493511382"; //http://blog.csdn.net/linfanhehe/article/details/58584953
    }

    public String getKey() {
//        return "2ab9071b06b9f739b950ddb41db2690d";
        return "43085bbaaf4ed933d0352580cbc0f134";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }

    public static void main(String[] args) throws Exception
    {

        WXPayConfig config = WXPayConfigImpl.getInstance();
//        System.out.println("this.certData: " + config.certData);
        config.getCertStream();
    }


}
