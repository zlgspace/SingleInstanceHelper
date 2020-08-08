package com.zlgspace.singleinstancehelper.test.entity;

import com.zlgspace.singleinstancehelper.annotation.SingleInstance;
import com.zlgspace.singleinstancehelper.test.em.DeviceType;

@SingleInstance("SingleDeviceState")
public class DeviceState {

    private String devName;

    private DeviceType devType;

    private boolean isRunning;


    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public DeviceType getDevType() {
        return devType;
    }

    public void setDevType(DeviceType devType) {
        this.devType = devType;
    }

    @Override
    public String toString() {
        return "{"+
                "\ndevName : "+devName+
                "\ndevType : "+devType+
                "\nisRunning : "+isRunning+
                "\n}";
    }
}
