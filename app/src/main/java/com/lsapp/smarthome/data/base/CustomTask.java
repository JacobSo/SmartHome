package com.lsapp.smarthome.data.base;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CustomTask {
    private String Condition;
    private String TriggerName;
    private int CreateTime;
    private String DeviceId;
    private boolean Enable;
    private int TriggerType;
    private int RunMin;
    private int Gear;
    private int Anion;
    private Condition conditionObj;

    private String gearText;
    private String anionText;

    public String getGearText() {
        if (Gear == 0) {
            return "关闭";

        } else if (Gear == 1) {
            return "睡眠模式";
        } else if (Gear == 2) {
            return "日常模式";
        }else{
            return "超强喷射模式";
        }
    }

    public String getAnionText() {
        if (Anion == 0) {
            return "关闭";

        } else if (Anion == 1) {
            return "普通";
        } else {
            return "加强";
        }
    }

    public void setAnionText(String anionText) {
        this.anionText = anionText;
    }

    public void setGearText(String gearText) {
        this.gearText = gearText;
    }

    public CustomTask.Condition getConditionObj() {
        Gson gson = new Gson();
        conditionObj = gson.fromJson(getCondition(), CustomTask.Condition.class);
        return conditionObj;
    }

    public void setConditionObj(CustomTask.Condition conditionObj) {
        this.conditionObj = conditionObj;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getTriggerName() {
        return TriggerName;
    }

    public void setTriggerName(String triggerName) {
        TriggerName = triggerName;
    }

    public int getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(int createTime) {
        CreateTime = createTime;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public boolean isEnable() {
        return Enable;
    }

    public void setEnable(boolean enable) {
        Enable = enable;
    }

    public int getTriggerType() {
        return TriggerType;
    }

    public void setTriggerType(int triggerType) {
        TriggerType = triggerType;
    }

    public int getRunMin() {
        return RunMin;
    }

    public void setRunMin(int runMin) {
        RunMin = runMin;
    }

    public int getGear() {
        return Gear;
    }

    public void setGear(int gear) {
        Gear = gear;
    }

    public int getAnion() {
        return Anion;
    }

    public void setAnion(int anion) {
        Anion = anion;
    }

    public class Condition {
        private String StartTime;
        private boolean IsRepeat;

        private int JqTriggerValue;
        private int Pm25TriggerValue;
        private int VocTriggerValue;
        private int JbTriggerValue;

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public boolean isRepeat() {
            return IsRepeat;
        }

        public void setRepeat(boolean repeat) {
            IsRepeat = repeat;
        }

        public int getJqTriggerValue() {
            return JqTriggerValue;
        }

        public void setJqTriggerValue(int jqTriggerValue) {
            JqTriggerValue = jqTriggerValue;
        }

        public int getPm25TriggerValue() {
            return Pm25TriggerValue;
        }

        public void setPm25TriggerValue(int pm25TriggerValue) {
            Pm25TriggerValue = pm25TriggerValue;
        }

        public int getVocTriggerValue() {
            return VocTriggerValue;
        }

        public void setVocTriggerValue(int vocTriggerValue) {
            VocTriggerValue = vocTriggerValue;
        }

        public int getJbTriggerValue() {
            return JbTriggerValue;
        }

        public void setJbTriggerValue(int jbTriggerValue) {
            JbTriggerValue = jbTriggerValue;
        }


    }


}
