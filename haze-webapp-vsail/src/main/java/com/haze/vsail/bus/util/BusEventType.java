package com.haze.vsail.bus.util;

public enum BusEventType {

    /**
     * 车辆上线
     */
    BUS_EVENT_ON(1),

    /**
     * 车辆下线
     */
    BUS_EVENT_OFF(2),

    /**
     * 车辆实时运行
     */
    BUS_EVENT_REAL(3),

    /**
     * 车辆注册
     */
    BUS_EVENT_REGISTER(4),

    /**
     * 车辆更新
     */
    BUS_EVENT_UPDATE(5),

    /**
     * 车辆删除
     */
    BUS_EVENT_DELETE(-1);

    private int eventCode;

    private BusEventType(int eventCode) {
        this.eventCode = eventCode;
    }

    public int getEventCode() {
        return this.eventCode;
    }

    public static BusEventType parser(int eventCode) {
        BusEventType[] types = BusEventType.values();
        for (BusEventType type : types) {
            if (type.getEventCode() == eventCode) {
                return type;
            }
        }
        throw new IllegalArgumentException("事件代码错误");
    }

    @Override
    public String toString() {
        return String.valueOf(eventCode);
    }
}
