//package com.pkh.schedule.util;
//
//import org.directwebremoting.*;
//import org.directwebremoting.proxy.dwr.Util;
//
//import javax.servlet.ServletContext;
//import java.util.Collection;
///**
// * Created by Administrator on 2016/11/16.
// */
//
//public class SendMessageUtil {
//
//    private static void sendMsg(String functionName, String pageUrl, ServerContext context, Object... params) {
//        Collection<ScriptSession> sessions = context.getScriptSessionsByPage(pageUrl);
//        if (sessions == null || sessions.size() == 0)
//            return;
//        Util util = new Util(sessions);
//        if (params == null) {
//            util.addFunctionCall(functionName);
//            return;
//        }
//        switch (params.length)
//        {
//            case 0 :
//                util.addFunctionCall(functionName);
//                break;
//            case 1 :
//                util.addFunctionCall(functionName, params[0]);
//                break;
//            case 2 :
//                util.addFunctionCall(functionName, params[0], params[1]);
//                break;
//            case 3 :
//                util.addFunctionCall(functionName, params[0], params[1], params[2]);
//                break;
//            case 4 :
//                util.addFunctionCall(functionName, params[0], params[1], params[2], params[3]);
//                break;
//            case 5 :
//                util.addFunctionCall(functionName, params[0], params[1], params[2], params[3], params[4]);
//                break;
//            case 6 :
//                util.addFunctionCall(functionName, params[0], params[1], params[2], params[3], params[4], params[5]);
//                break;
//            case 7 :
//            default :
//                util.addFunctionCall(functionName, params[0], params[1], params[2], params[4], params[5], params[6], params[7]);
//                break;
//        }
//    }
//
//    public static void sendMsg(String functionName, String pageUrl, Object... params) {
//        //得到上下文
//        WebContext context = WebContextFactory.get();
//        sendMsg(functionName, pageUrl, context, params);
//    }
//
//    public static void sendMsgOther(String functionName, String pageUrl, ServletContext context, Object... params) {
//        //得到上下文
//        ServerContext serverContext = ServerContextFactory.get(context);
//
//        sendMsg(functionName, pageUrl, serverContext, params);
//    }
//}
