package com.haze.vsail.bus.event;

import com.haze.vsail.bus.util.BusInfo;
import org.springframework.context.ApplicationEvent;


public class BusEvent extends ApplicationEvent {

    public BusEvent(BusInfo busInfo) {
        super(busInfo);
        this.busInfo = busInfo;
    }

    private BusInfo busInfo;

    public BusInfo getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(BusInfo busInfo) {
        this.busInfo = busInfo;
    }
}
