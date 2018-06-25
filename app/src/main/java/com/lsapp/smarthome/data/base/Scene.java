package com.lsapp.smarthome.data.base;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.utils.ColorUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/15.
 */
public class Scene implements Serializable {
    private String SpaceName;
    private String UserId;
    private String SpaceId;
    private String DeviceId;
    private String ActionId;
    private String CreateUser;
    private String CreateTime;
    private int VocType = -1;
    private String  VocValue;//loop
    private int SpaceType = 0;
    private int SpaceAnim;
    private int voc = 100;
    private int vocColor;
    private int emptyImage;
    private int checkairConnect;
    private int airclearConnect;
    private ArrayList<CheckData> SpaceData;
    private ArrayList<CheckData> list;//loop
    private Control airClerState;
    private String VocTime;
    private String vocText;

    private int IsShareSpace;
    private int IsAllowOperat;


    public Scene(String spaceId, String spaceName, String userId) {
        SpaceId = spaceId;
        SpaceName = spaceName;
        UserId = userId;
    }

    public int getIsShareSpace() {
        return IsShareSpace;
    }

    public void setIsShareSpace(int isShareSpace) {
        IsShareSpace = isShareSpace;
    }

    public int getIsAllowOperat() {
        return IsAllowOperat;
    }

    public void setIsAllowOperat(int isAllowOperat) {
        IsAllowOperat = isAllowOperat;
    }

    public ArrayList<CheckData> getList() {
        return list;
    }

    public void setList(ArrayList<CheckData> list) {
        this.list = list;
    }

    public String getVocValue() {
        return VocValue;
    }

    public void setVocValue(String vocValue) {
        VocValue = vocValue;
    }

    public String getVocTime() {
        return VocTime;
    }

    public void setVocTime(String vocTime) {
        VocTime = vocTime;
    }

    public int getCheckairConnect() {
        return checkairConnect;
    }

    public void setCheckairConnect(int checkairConnect) {
        this.checkairConnect = checkairConnect;
    }

    public int getAirclearConnect() {
        return airclearConnect;
    }

    public void setAirclearConnect(int airclearConnect) {
        this.airclearConnect = airclearConnect;
    }

    public int getEmptyImage() {
        int temp;
        if (getSpaceType() == 0) {
            temp = R.drawable.empty_living;
        } else if (getSpaceType() == 1) {
            temp = R.drawable.empty_canteen;
        } else {
            temp = R.drawable.empty_bedroom;
        }
        return temp;
    }

    public String getVocText() {
        if (VocType == 0) {
            return "优";
        } else if (VocType == 1) {
            return "良";
        } else if (VocType == 2) {
            return "中";
        } else if (VocType == 3) {
            return "差";
        } else
            return "未知";
    }

    public void setVocText(String vocText) {
        this.vocText = vocText;
    }

    public void setEmptyImage(int emptyImage) {
        this.emptyImage = emptyImage;
    }

    public int getVocColor() {
        if (VocType == 0) {
            return ColorUtil.getQualityGreen();
        } else if (VocType == 1) {
            return ColorUtil.getQualityBlue();
        } else if (VocType == 2) {
            return ColorUtil.getQualityYellow();
        } else if (VocType == 3) {
            return ColorUtil.getQualityRed();
        } else
            return ColorUtil.getQualityGreen();
    }

    public int getSpaceAnim() {
        int temp;
        if (getSpaceType() == 0) {
            if (getDeviceId() == null || getSpaceData() == null || getSpaceData().size() == 0) {
                temp = R.drawable.empty_living;
            } else {
                if(VocType==0){
                    temp = R.drawable.anim_livingroom_a;
                }else if (VocType==1) {
                    temp = R.drawable.anim_livingroom_a;
                }else if (VocType==2) {
                    temp = R.drawable.anim_livingroom_b;
                }else if(VocType==3){
                    temp = R.drawable.anim_livingroom_c;
                }else{//-1
                    temp = R.drawable.empty_living;
                }
            }
        } else if (getSpaceType() == 1) {
            if (getDeviceId() == null || getSpaceData() == null || getSpaceData().size() == 0) {
                temp = R.drawable.empty_canteen;
            } else {
                if(VocType==0){
                    temp = R.drawable.anim_canteen_a;
                }else if (VocType==1) {
                    temp = R.drawable.anim_canteen_a;
                }else if (VocType==2) {
                    temp = R.drawable.anim_canteen_b;
                }else if(VocType==3){
                    temp = R.drawable.anim_canteen_c;
                }else{//-1
                    temp = R.drawable.empty_canteen;
                }
            }

        } else {
            if (getDeviceId() == null || getSpaceData() == null || getSpaceData().size() == 0) {
                temp = R.drawable.empty_bedroom;
            } else {
                if(VocType==0){
                    temp = R.drawable.anim_bedroom_a;
                }else if (VocType==1) {
                    temp = R.drawable.anim_bedroom_a;
                }else if (VocType==2) {
                    temp = R.drawable.anim_bedroom_b;
                }else if(VocType==3){
                    temp = R.drawable.anim_bedroom_c;
                }else{//-1
                    temp = R.drawable.empty_bedroom;
                }
            }

        }
        return temp;
    }

    public Control getAirClerState() {
        return airClerState==null?new Control(0,0,0,0,0,0,0,0):airClerState;
    }

    public void setAirClerState(Control airClerState) {
        this.airClerState = airClerState;
    }

    public void setSpaceAnim(int spaceAnim) {
        SpaceAnim = spaceAnim;
    }

    public int getSpaceType() {
        return SpaceType;
    }

    public void setSpaceType(int spaceType) {
        SpaceType = spaceType;
    }

    public void setVocColor(int vocColor) {
        this.vocColor = vocColor;
    }

    public int getVocType() {
        return VocType;
    }

    public void setVocType(int vocType) {
        VocType = vocType;
    }

    public int getVoc() {
        return voc;
    }

    public void setVoc(int voc) {
        this.voc = voc;
    }

    public String getActionId() {
        return ActionId;
    }

    public void setActionId(String actionId) {
        ActionId = actionId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public ArrayList<CheckData> getSpaceData() {
        return SpaceData;
    }

    public void setSpaceData(ArrayList<CheckData> spaceData) {
        SpaceData = spaceData;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getSpaceName() {
        return SpaceName;
    }

    public void setSpaceName(String spaceName) {
        SpaceName = spaceName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSpaceId() {
        return SpaceId;
    }

    public void setSpaceId(String spaceId) {
        SpaceId = spaceId;
    }
}
