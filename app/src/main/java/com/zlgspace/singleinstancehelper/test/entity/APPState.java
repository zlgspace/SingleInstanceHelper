package com.zlgspace.singleinstancehelper.test.entity;

import com.zlgspace.singleinstancehelper.annotation.SingleInstance;

/**
 * 只要生成类取的名称一致，即可将多个Entity合并成一个单例类
 * 但有一点需要注意，每个了类中的public函数必须不一致，否则生成类报错
 */
@SingleInstance("SingleDeviceState")
public class APPState {

    private boolean isAppRunning = false;

    private String curProcesses[];

    public boolean isAppRunning() {
        return isAppRunning;
    }

    public void setAppRunning(boolean running) {
        isAppRunning = running;
    }

    public String[] getCurPras() {
        return curProcesses;
    }

    public void setCurPras(String[] curPras) {
        this.curProcesses = curPras;
    }
}
