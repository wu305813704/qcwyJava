package com.qcwy.utils;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

/**
 * Created by KouKi on 2017/2/17.
 */
public class JpushUtils {
    //快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知
    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }

    //构建推送对象：所有平台，推送目标是别名为 alias，通知内容为 Msg。
    public static PushPayload buildPushObject_all_alias_alert(String alias, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(msg))
                .build();
    }

    //构建推送对象：平台是 Android，目标是 别名为alias 的设备，内容是 Android 通知 msg，并且标题为 TITLE
    public static PushPayload buildPushObject_android_alias_alertWithTitle(String alias, String title, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.android(msg, title, null))
                .build();
    }

    //构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，
    // 推送内容同时包括通知与消息 - 通知信息是 alert，角标数字为 num，通知声音为 sound，
    // 并且附加字段 from = from；消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，
    // 消息是 JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(
            String tag1, String tag_all, String alert, int num, String sound, String from, String msgContent) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(tag1, tag_all))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .setBadge(num)
                                .setSound(sound)
                                .addExtra("from", from)
                                .build())
                        .build())
                .setMessage(Message.content(msgContent))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    //    //构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集），
//    // 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
//    public static PushPayload buildPushObject_ios_audienceMore_messageWithAlias() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android_ios())
//                .setAudience(Audience.newBuilder()
//                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
//                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
//                        .build())
//                .setMessage(AppOrderMessage.newBuilder()
//                        .setMsgContent("MSG_CONTENT")
//                        .addExtra("from", "JPush")
//                        .build())
//                .build();
//    }
    //构建推送对象：平台是 Andorid 与 iOS，推送目标是 alias
    // 推送内容是 - 内容为 Msg 的消息
    public static PushPayload buildPushObject_ios_audienceMore_messageWithAlias(String alias, String title, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.alias(alias))
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(title)
                        .setMsgContent(msg)
                        .build())
                .build();
    }

    /**
     * 通知+消息
     *
     * @param alias   目标别名
     * @param title   通知标题
     * @param msg     通知内容
     * @param tag     消息标题
     * @param content 消息内容
     * @return
     */
    public static PushPayload buildPushObject_all_alert_messageWithAlias(String alias, String title, String msg, String tag, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msg)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .setAlert(msg)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(tag)
                        .setMsgContent(content)
                        .build())
                .build();
    }

}
