package com.lsapp.smarthome.network;

import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.ControlData;
import com.lsapp.smarthome.data.CustomData;
import com.lsapp.smarthome.data.CustomTaskData;
import com.lsapp.smarthome.data.DateCheckData;
import com.lsapp.smarthome.data.DeviceDetailData;
import com.lsapp.smarthome.data.DeviceListData;
import com.lsapp.smarthome.data.GuideDeviceData;
import com.lsapp.smarthome.data.MessageData;
import com.lsapp.smarthome.data.RecoveryDeviceData;
import com.lsapp.smarthome.data.TotalData;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.SceneData;
import com.lsapp.smarthome.data.HistoryData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/11.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("Login/UserLogin")
    Observable<AccountData> login(@Field("UserName") String account,
                                  @Field("IsThirdParty") int way,
                                  @Field("MobileID") String deviceId,
                                  @Field("MobileType") String os,
                                  @Field("RealName") String name,
                                  @Field("ValidateCode") String code,
                                  @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("Login/UserLoginNoCode")
    Observable<AccountData> login(@Field("UserName") String account,
                                  @Field("IsThirdParty") int way,
                                  @Field("MobileID") String deviceId,
                                  @Field("MobileType") String os,
                                  @Field("RealName") String name,
                                  @Field("pwd") String pwd);

    @GET("Login/GetVerificatCode")
    Observable<BaseData> smsCode(@Query("phoneNumber") String number);

    @GET("Logout/UserLogout")
    Observable<BaseData> logout(@Query("SessionKey") String key);

    @GET("GetUserInfo/UserInfo")
    Observable<AccountData> getUserInfo(@Query("UserId") String uid, @Query("SessionKey") String key);

    @FormUrlEncoded
    @POST("UpdateUser/UpdateUserInfo")
    Observable<BaseData> updateUserInfo(@Field("UserId") String account,
                                        @Field("MobileID") String deviceId,
                                        @Field("RealName") String nick,
                                        @Field("Mobile") String phone,
                                        @Field("Birthday") String birth,
                                        @Field("Sex") String sex,
                                        @Field("Province") String Province,
                                        @Field("City") String city,
                                        @Field("Area") String area,
                                        @Field("Address") String address,
                                        @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("UserSpace/AddUserSpace")
    Observable<SceneData> addScene(@Field("UserId") String uid,
                                   @Field("SpaceName") String sceneNam,
                                   @Field("SpaceType") int type,
                                   @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("UserAndItemRrelated/UserRrelated")
    Observable<BaseData> addDevice(@Field("SpaceId") String sid,
                                   @Field("UserId") String uid,
                                   @Field("Mac") String mac,
                                   @Field("SpaceName") String SpaceName,
                                   @Field("isrecovery") int recovery,
                                   @Field("SessionKey") String key);

    @GET("UserSpace/GetUserSpaceList")
    Observable<SceneData> getScene(@Query("UserId") String uid,
                                   @Query("SessionKey") String key);


    @FormUrlEncoded
    @POST("UserSpace/UpdateUserSpaceName")
    Observable<BaseData> updateScene(@Field("UserId") String uid,
                                     @Field("SpaceId") String sceneId,
                                     @Field("SpaceName") String sceneName,
                                     @Field("SpaceType") int type,
                                     @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("UserSpace/DelUserSpace")
    Observable<BaseData> deleteScene(@Field("UserId") String uid,
                                     @Field("SpaceId") String sid,
                                     @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("UserAndItemRrelated/DeleteRrelated")
    Observable<BaseData> deleteDevice(@Field("UserId") String uid,
                                      @Field("SpaceId") String sid,
                                      @Field("Mac") String mac,
                                      @Field("SessionKey") String key);

    @GET("Msg/GetMsg")
    Observable<MessageData> getMessage(@Query("UserId") String uid,
                                       @Query("NextPageStartSendTime") int start,
                                       @Query("SessionKey") String key);
//account and member

    @FormUrlEncoded
    @POST("BindingAccount/BindAccount")
    Observable<BaseData> bindAccount(@Field("UserName") String uid,
                                     @Field("RealName") String RealName,
                                     @Field("IsThirdParty") int way,
                                     @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("BindingAccount/RemoveBindAccount")
    Observable<BaseData> unbindAccount(@Field("UserName") String name,
                                       @Field("IsThirdParty") int way,
                                       @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("RelationAccount/Relation")
    Observable<BaseData> addMember(@Field("Mobile") String mobile,
                                   @Field("Nickname") String Nickname,
                                   @Field("IsAllowOperat") int opera,
                                   @Field("issendSMS") int sms,
                                   @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("RelationAccount/ConfirmRelationAccount")
    Observable<BaseData> confirmBeMember(@Field("ConfirmResult") int result,
                                         @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("RelationAccount/RevokeRelationAccount")
    Observable<BaseData> unbindingMember(@Field("UserName") String name,
                                         @Field("SessionKey") String key);


    @GET("GetUserInfo/GetRelationUser")
    Observable<AccountData> getMyMembers(@Query("SessionKey") String key);

    @GET("Msg/GetLastMsgTime")
    Observable<BaseData> getNewFeed(@Query("SessionKey") String key);

    @GET("Msg/GetMsgConfig")
    Observable<AccountData> getPushConfig(@Query("SessionKey") String key);

    @FormUrlEncoded
    @POST("Msg/SetMsgConfig")
    Observable<BaseData> setPushConfig(@Field("IsReceiveWarn") int warn,
                                       @Field("IsReceiveLog") int log,
                                       @Field("IsReceiveSaleMsg") int news,
                                       @Field("SessionKey") String key);


    @FormUrlEncoded
    @POST("UserSpace/GetSpaceDeviceListN")
    Observable<DeviceListData> getDeviceList(@Field("UserId") String uid,
                                             @Field("SpaceId") String sid,
                                             @Field("SessionKey") String key);

    @GET("UserSpace/GetSpaceDeviceListDetail")
    Observable<DeviceListData> getDeviceDetail(@Query("SessionKey") String key,
                                               @Query("mac") String mac,
                                               @Query("ItemType") int type);

    @FormUrlEncoded
    @POST("AirCheckControl/setUpInterval")
    Observable<DeviceListData> setDeviceTime(@Field("Interval") int seconds,
                                             @Field("DeviceId") String did,
                                             @Field("SessionKey") String key);


    @GET("Trigger/GetTriggerObjs")
    Observable<CustomData> getCustiomCheckData(@Query("SessionKey") String key);

    @FormUrlEncoded
    @POST("Trigger/DelTrigger")
    Observable<DeviceListData> deleteCustomTask(@Field("CreateTime") int createTime,
                                                @Field("DeviceId") String did,
                                                @Field("SessionKey") String key);

    @FormUrlEncoded
    @POST("Trigger/AddTriggers")
    Observable<DeviceListData> addCustomTask(@Field("SessionKey") String key,
                                             @Field("TriggerName") String TriggerName,
                                             @Field("DeviceId") String DeviceId,
                                             @Field("Enable") int Enable,
                                             @Field("TriggerType") int TriggerType,
                                             @Field("RunMin") int RunMin,
                                             @Field("Gear") int Gear,
                                             @Field("Anion") int Anion,
                                             @Field("Condition") String Condition,
                                             @Field("checkdevmac") String mac);

    @GET("Trigger/getTriggerList")
    Observable<CustomTaskData> getCustomList(@Query("DeviceId") String DeviceId,
                                             @Query("SessionKey") String key);

    @FormUrlEncoded
    @POST("Trigger/setTriggerEnable")
    Observable<BaseData> setCustomEnable(@Field("SessionKey") String key,
                                         @Field("DeviceId") String DeviceId,
                                         @Field("Enable") int Enable,
                                         @Field("CreateTime") int CreateTime);

    @GET("DetectData/GetSpaceTrendDataByDataType")
    Observable<DeviceDetailData> getChartDetail(@Query("SessionKey") String key,
                                                @Query("DeviceId") String DeviceId,
                                                @Query("DataType") int DataType,
                                                @Query("hours") int hours);

    @FormUrlEncoded
    @POST("Air/AirControl")
    Observable<BaseData> airControl(@Field("SessionKey") String key,
                                    @Field("ActionId") String TriggerName,
                                    @Field("Purifier") int DeviceId,
                                    @Field("IonMode") int Enable,
                                    @Field("Timer") int TriggerType,
                                    @Field("PanelLight") int PanelLight
    );

    @FormUrlEncoded
    @POST("AirCheckControl/controlLedLight")
    Observable<BaseData> lightControl(@Field("SessionKey") String key,
                                      @Field("DeviceId") String TriggerName,
                                      @Field("FanOn") int wind,
                                      @Field("WorklightOn") int work);

    @FormUrlEncoded
    @POST("AirClear/OnekeyOptimize")
    Observable<BaseData> quickTurn(@Field("SessionKey") String key,
                                   @Field("level") int level,
                                   @Field("airclearmac") String mac);

    @GET("CalendarAirData/GetMonthVocData")
    Observable<DateCheckData> getCalendar(@Query("SessionKey") String key,
                                          @Query("DeviceId") String DeviceId,
                                          @Query("ActionId") String ActionId,
                                          @Query("yearandmonth") String DataType
    );

    @GET("CalendarAirData/getminmaxValueByDay")
    Observable<HistoryData> getDateData(@Query("SessionKey") String key,
                                        @Query("DeviceId") String DeviceId,
                                        @Query("daytime") String date);

    @GET("AirClear/getAirClearState")
    Observable<ControlData> getAirStatus(@Query("SessionKey") String key,
                                         @Query("airClearMac") String airClearMac);

    @GET("AirClear/getCheckLightState")
    Observable<ControlData> getCheckLightState(@Query("SessionKey") String key,
                                               @Query("aircheckMac") String aircheckMac);

    @GET("CalendarAirData/getminmaxSevenDayValue")
    Observable<DeviceDetailData> getWeekChart(@Query("SessionKey") String key,
                                              @Query("DeviceId") String DeviceId,
                                              @Query("DataType") int DataType);

    @GET("UserSpace/GetUserSpaceListToMgr")
    Observable<SceneData> getSceneModify(@Query("SessionKey") String key,
                                         @Query("UserId") String uid);

    @FormUrlEncoded
    @POST("Msg/delMsg")
    Observable<BaseData> deleteMessage(@Field("SessionKey") String key,
                                       @Field("ReceiveUserName") String name,
                                       @Field("SendTime") String time,
                                       @Field("ReceiveThirdParty") String party);

    @FormUrlEncoded
    @POST("UpdateUser/UpUserAllowOperat")
    Observable<BaseData> modifyPermission(@Field("SessionKey") String key,
                                          @Field("UserName") String name,
                                          @Field("IsThirdParty") int time,
                                          @Field("OperatType") int party);


    @GET("Guide/getDeviceInfo")
    Observable<GuideDeviceData> guideGetDeviceList(@Query("SessionKey") String key,
                                                   @Query("macs") String macs,
                                                   @Query("UserId") String uid);

    @FormUrlEncoded
    @POST("Guide/createSpaceAndBindMac")
    Observable<BaseData> guideBinging(@Field("SessionKey") String key,
                                      @Field("UserId") String uid,
                                      @Field("SpaceId") String spaceId,
                                      @Field("SpaceName") String spaceName,
                                      @Field("SpaceType") int spaceType,
                                      @Field("DeviceId") String deviceId,
                                      @Field("ActionId") String actionId,
                                      @Field("DeviceSku") String deviceSku,
                                      @Field("ActionSku") String actionSku);

    @GET("UnRrelatedLog/getUnRrelatedLogList")
    Observable<RecoveryDeviceData> getUnbindingList(@Query("SessionKey") String key,
                                                    @Query("UserId") String macs);

    @FormUrlEncoded
    @POST("UserAndItemRrelated/recoveryMacIntoDb")
    Observable<BaseData> macSubmit(@Field("SessionKey") String key,
                                   @Field("mac") String mac,
                                   @Field("Itemtype") int type);

    @FormUrlEncoded
    @POST("Air/replaceFilterConfirm")
    Observable<BaseData> finishFilter(@Field("SessionKey") String key,
                                      @Field("ActionId") String ActionId);

    @FormUrlEncoded
    @POST("Pwd/uppwd")
    Observable<BaseData> passwordSet(@Field("SessionKey") String key,
                                     @Field("UserName") String user,
                                     @Field("pwd") String pwd
    );

    @GET("RoundRobinData/GetRoundRobinData")
    Observable<SceneData> realTimeData(@Query("SessionKey") String key,
                                      @Query("DeviceId") String DeviceId);
}
