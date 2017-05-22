package com.pkh.schedule.dwr;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/28.
 */
public class SendMessage {

    public static void getMessage(String data) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data = sdf.format(new Date())+" : " +data;
        final String finalData = data;
        Runnable run = new Runnable() {
            private ScriptBuffer script = new ScriptBuffer();
            public void run() {
                // 设置要调用的 js及参数
                script.appendCall("getDate", finalData);
                // 得到所有ScriptSession
                Collection<ScriptSession> sessions = Browser
                        .getTargetSessions();
                // 遍历每一个ScriptSession
                for (ScriptSession scriptSession : sessions) {
                    scriptSession.addScript(script);
                }
            }
        };
        // 执行推送
        Browser.withAllSessions(run);
    }
}
