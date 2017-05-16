import com.qcwy.RedisClient;
import com.qcwy.utils.wx.UnifiedOrderResponseData;
import com.qcwy.utils.wx.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Created by KouKi on 2017/3/28.
 */
@ClientEndpoint
public class Test {
    @Autowired
    private RedisClient jedis;

    private String deviceId;
    private Session session;

    public Test() {

    }

    public Test(String deviceId) {
        this.deviceId = deviceId;
    }

    protected boolean start() {
        WebSocketContainer Container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/qcwy/wxWebSocket/123";
        System.out.println("Connecting to " + uri);
        try {
            session = Container.connectToServer(Test.class, URI.create(uri));
            session.getBasicRemote().sendText("111");
            System.out.println("count: " + deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx0d172b65b0a49776]]></appid><mch_id><![CDATA[1384760002]]></mch_id><device_info><![CDATA[WEB]]></device_info><nonce_str><![CDATA[M04TbaJ7KP7efpw1]]></nonce_str><sign><![CDATA[961092C31F1AFD7E332318620BD686CA]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx20170415091554854323d9b30268249926]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
//        UnifiedOrderResponseData data = (UnifiedOrderResponseData) XmlUtils.getObjectFromXML(xml,UnifiedOrderResponseData.class);
        Map<String, Object> map = XmlUtils.getMapFromXML(xml);
//        System.out.println(data.getDevice_info());
        UnifiedOrderResponseData data = new UnifiedOrderResponseData();
        XmlUtils.getObjectFromMap(map, data);
        System.out.println(data.getAppid());

//        AppUser user = new AppUser();
//        user.setJob_no("12321/");
//        user.setName("243dskafj");
//        AppUser user1 = new AppUser();
//        user1.setName("444");
//        List<AppUser> userList = new ArrayList<>();
//        userList.add(user);
//        userList.add(user1);
//        Transaction transaction = jedis.multi();
//        transaction.set("user".getBytes(), SerializeUtils.serialize(userList));
//        transaction.exec();
//        List<AppUser> appUsers = (List<AppUser>) SerializeUtils.unserialize(jedis.get("user".getBytes()));
//        System.out.println(appUsers.get(0).getName());
//        System.out.println(appUsers.get(1).getName());
    }

}
