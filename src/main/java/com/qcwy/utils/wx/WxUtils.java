package com.qcwy.utils.wx;

import com.google.gson.Gson;
import com.qcwy.entity.WxAccessToken;
import com.qcwy.entity.WxInfo;
import com.qcwy.entity.WxUser;
import com.qcwy.entity.bg.SystemInfo;
import com.qcwy.service.OrderService;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by KouKi on 2017/3/7.
 */
public class WxUtils {
    private static HttpClient client = new HttpClient();

    //get请求
    private static String getResult(String url) throws IOException{
        // 设置代理服务器地址和端口
        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        HttpMethod method = new GetMethod(url.toString());
        //使用POST方法
        //HttpMethod method = new PostMethod("http://java.sun.com");
        client.executeMethod(method);
        String result = method.getResponseBodyAsString();
        // 编码后的json
        result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
        //释放连接
        method.releaseConnection();
        return result;
    }

    private static Gson gson = new Gson();

    public static WxAccessToken getAccessToken(WxInfo wxInfo, String code) throws IOException {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
        url.append(wxInfo.getAppid() + "&secret=")
                .append(wxInfo.getSecret() + "&code=")
                .append(code + "&grant_type=authorization_code");
        String result = getResult(url.toString());
        WxAccessToken token = gson.fromJson(result, WxAccessToken.class);
        return token;
    }

    //获取用户信息
    public static WxUser getUserInfo(String accessToken, String openId) throws IOException {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=")
                .append(accessToken + "&openid=")
                .append(openId + "&lang=zh_CN");
        String result = getResult(url.toString());
        WxUser wxUser = gson.fromJson(result, WxUser.class);
        System.out.println(wxUser.getNickname());
        return wxUser;
    }

    //预付款
    public static JsonResult<?> getAdvancePackege(String appid, String body, String orderNo, int totalFee, String ip, String tradeType, String openid) throws IOException, ParserConfigurationException, SAXException {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        // 设置代理服务器地址和端口
        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        //使用POST方法
        PostMethod method = new PostMethod(url);
        UnifiedOrderRequestData data = new UnifiedOrderRequestData();
        data.setAppid(appid);
        data.setMch_id(WeixinPayConfig.MCH_ID);
        data.setDevice_info("WEB");
        data.setNonce_str(createRandom(false, 32));
        data.setBody(body);
        data.setOut_trade_no(orderNo);
        data.setTotal_fee(totalFee);
        data.setSpbill_create_ip(ip);
        data.setNotify_url(WeixinPayConfig.NOTIFY_URL);
        data.setTrade_type(tradeType);
        data.setOpenid(openid);
        data.setSign(getSign(data));
        String payXML = castDataToXMLString(data);
        method.setRequestBody(payXML);
        String responseString = null;
        int statusCode = client.executeMethod(method);
        if (statusCode == HttpStatus.SC_OK) {
            InputStream inputStream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            responseString = stringBuffer.toString();
        }
        //释放连接
        method.releaseConnection();
        Map<String, Object> map = XmlUtils.getMapFromXML(responseString);
        UnifiedOrderResponseData responseData = new UnifiedOrderResponseData();
        XmlUtils.getObjectFromMap(map, responseData);
        return getAppPackage(responseData);
    }

    //MD5进行加密
    public final static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 利用反射获取Java对象的字段并进行加密，过滤掉sign字段
     *
     * @param data
     * @return return:String
     */
    public static String getSign(Object data) {
        StringBuilder stringA = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        Field[] fields = data.getClass().getDeclaredFields();
        Method[] methods = data.getClass().getDeclaredMethods();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field != null && !fieldName.equals("sign")) {
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                for (Method method : methods) {
                    if (method.getName().equals(getMethodName)) {
                        try {
                            if (method.invoke(data, new Object[]{}) != null && method.invoke(data, new Object[]{}).toString().length() != 0) {
                                map.put(fieldName, method.invoke(data, new Object[]{}).toString());
                            }
                        } catch (IllegalAccessException | IllegalArgumentException
                                | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        List<Map.Entry<String, String>> mappingList;
        //通过ArrayList构造函数把map.entrySet()转换成list
        mappingList = new ArrayList<>(map.entrySet());
        //通过比较器实现比较排序
        Collections.sort(
                mappingList,
                Comparator.comparing(Map.Entry::getKey)
        );
        for (Map.Entry<String, String> mapping : mappingList) {
            stringA.append("&").append(mapping.getKey()).append("=").append(mapping.getValue());
        }
        String stringSignTemp = stringA.append("&key=").append(WeixinPayConfig.KEY).substring(1);
        return MD5(stringSignTemp).toUpperCase();
    }

    //获取随机字符串
    public static String createRandom(boolean numberFlag, int length) {
        StringBuilder retStr = new StringBuilder(length);
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        for (int i = 0; i < length; i++) {
            double dblR = Math.random() * len;
            int intR = (int) Math.floor(dblR);
            char c = strTable.charAt(intR);
            retStr.append(c);
        }
        return retStr.toString().toUpperCase();
    }

    /**
     * 将java对象转换为XML字符串
     *
     * @param object
     * @return return:String
     */
    public static String castDataToXMLString(Object object) throws UnsupportedEncodingException {
        //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        //为类重命名
        xStreamForRequestPostData.alias("xml", object.getClass());
        //将要提交给API的数据对象转换成XML格式数据Post给API
        return new String(xStreamForRequestPostData.toXML(object).getBytes(), "ISO8859-1");
    }

    /**
     * 把XML字符串转换为统一下单响应数据对象
     *
     * @param requestString
     * @return return:UnifiedOrderResponseData
     */
    public static UnifiedOrderResponseData castXMLStringToUnifiedOrderResponseData(String requestString) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("xml", UnifiedOrderResponseData.class);
        return (UnifiedOrderResponseData) xStream.fromXML(requestString);
    }

    /**
     * 把XML字符串转换为统一下单回调接口请求数据对象
     *
     * @param requestString
     * @return return:UnifiedOrderNotifyRequestData
     */
    public static UnifiedOrderNotifyRequestData castXMLStringToUnifiedOrderNotifyRequestData(String requestString) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("xml", UnifiedOrderNotifyRequestData.class);
        return (UnifiedOrderNotifyRequestData) xStream.fromXML(requestString);
    }

    /**
     * 把XML字符串转换为统一下单回调接口响应数据对象
     *
     * @param requestString
     * @return return:UnifiedOrderNotifyResponseData
     */
    public static UnifiedOrderNotifyResponseData castXMLStringToUnifiedOrderNotifyResponseData(String requestString) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("xml", UnifiedOrderNotifyResponseData.class);
        return (UnifiedOrderNotifyResponseData) xStream.fromXML(requestString);
    }

    /**
     * 根据统一下单接口返回的数据，生成前台JS-SDK所需的数据包
     *
     * @param responseData
     * @return JSONObject
     */
    private static JsonResult<?> getAppPackage(
            UnifiedOrderResponseData responseData) {
        if (null == responseData) {
            return null;
        }
        // return_code 为 FAIL
        if (responseData.getReturn_code() == null
                || !WeixinConstant.SUCCESS
                .equals(responseData.getReturn_code())) {
            return new JsonResult<>(responseData.getReturn_msg());
        }
        // result_code 为 FAIL
        if (responseData.getResult_code() == null
                || !WeixinConstant.SUCCESS
                .equals(responseData.getResult_code())) {
            return new JsonResult<>(responseData.getErr_code_des());
        }
        String responseSign = getSign(responseData);
        // 签名错误
        if (!responseSign.equals(responseData.getSign())) {
            return new JsonResult<>("签名错误");
        }
        // 将数据封装成JS-SDK需要的形式返回前台
        // appId 是 String(16) wx8888888888888888 商户注册具有支付权限的公众号成功后即可获得
        String appId = WeixinPayConfig.APP_ID;
        // 时间戳 timeStamp 是 String(32) 1414561699
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 随机字符串 nonceStr 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
        // 随机字符串，不长于32位
        String nonceStr = createRandom(false, 32);
        // 订单详情扩展字符串 package 是 String(128) prepay_id=123456789
        // 微信统一下单接口返回的prepay_id参数值
        String packageStr = "prepay_id=" + responseData.getPrepay_id();
        // 签名方式 signType 是 String(32) MD5 签名算法
        String signType = WeixinConstant.SIGN_TYPE;
        // 签名 paySign 是 String(64) C380BEC2BFD727A4B6845133519F3AD6 签名
        GetBrandWCPayRequestData getBrandWCPayRequestData = new GetBrandWCPayRequestData(
                appId, timeStamp, nonceStr, packageStr, signType);
        getBrandWCPayRequestData.setPaySign(getSign(getBrandWCPayRequestData));
        return new JsonResult<>(getBrandWCPayRequestData);
    }

    /**
     * 验证微信支付结果回调，并更新本地订单状态
     *
     * @param requestData 微信回调响应的数据
     * @return
     */
    public static String getCallbackResponseData(
            UnifiedOrderNotifyRequestData requestData, OrderService orderService) {
        String responseData;
        if (null == requestData) {
            responseData = setXML(WeixinConstant.FAIL, "data is null or empty");
            return responseData;
        }
        String responseSign = getSign(requestData);
        do {
            // 判断签名－以防在网络传输过程中被篡改
            if (responseSign == null
                    || !responseSign.equals(requestData.getSign())) {
                responseData = setXML(WeixinConstant.FAIL, "invalid sign");
                break;
            }
            // 判断微信回调参数完整性
            // 本地商户订单号
            String outTradeNo = requestData.getOut_trade_no();
            // 微信订单号
            String transactionId = requestData.getTransaction_id();
            if (requestData.getReturn_code() == null
                    || requestData.getResult_code() == null
                    || requestData.getBank_type() == null
                    || requestData.getTotal_fee() == null
                    || StringUtils.isEmpty(outTradeNo)
                    || StringUtils.isEmpty(transactionId)) {
                responseData = setXML(WeixinConstant.FAIL, "important param is null");
                break;
            }
            // 支付失败
            if (WeixinConstant.FAIL.equals(requestData.getResult_code())) {
                responseData = setXML(WeixinConstant.FAIL, "weixin pay fail");
                break;
            }
            // 数据持久化操作
            // 更新本地订单状态为微信支付(11-已付款)
            boolean isOk = orderService.pay(Integer.valueOf(requestData.getOut_trade_no()), 0);// 0微信支付标识
            if (isOk) {
                responseData = setXML(WeixinConstant.SUCCESS, "OK");
                break;
            } else {
                responseData = setXML(WeixinConstant.FAIL, "update bussiness outTrade fail");
                break;
            }
        } while (false);
        return responseData;
    }

    /**
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     * @author KouKi
     * @date
     * @Description：返回给微信的参数
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }
}
