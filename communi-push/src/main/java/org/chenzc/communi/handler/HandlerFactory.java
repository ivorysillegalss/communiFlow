package org.chenzc.communi.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class HandlerFactory {

    private final HashMap<String, Handler> handlers = new HashMap<>();

    public void putHandler(String channelCode,Handler handler){
        handlers.put(channelCode,handler);
    }

    public Handler route(String channelCode){
        return handlers.get(channelCode);
    }

}
