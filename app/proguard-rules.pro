-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验  Android不需要preverify，去掉这一步可加快混淆速度
-verbose                                                                        # 混淆时是否记录日志,有了verbose这句话，混淆后就会生成映射文件
-printmapping proguardMapping.txt                                              # 包含有类名->混淆后类名的映射关系然后使用printmapping指定映射文件的名称
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法
-keepattributes *Annotation*                                                    # 保护代码中的Annotation不被混淆，这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature                                                       # 避免混淆泛型，这在JSON实体映射时非常重要，比如fastJson
-keepattributes SourceFile,LineNumberTable                                      # 抛出异常时保留代码行号，在异常分析中可以方便定位

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

#如果有引用v4包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**

-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {                                                       # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class * implements android.os.Parcelable {                                            # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

-keep public class * extends android.view.View {                                            # 保留自定义控件（继承自View）不被混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class **.R$* {                                                               # 对于R（资源）下的所有类及其方法，都不能被混淆
    *;
}
-keepclassmembers class * {                                                         # 对于带有回调函数onXXEvent的，不能被混淆
    void *(***Event);
}
# 保留实体类和成员不被混淆
-keep public class com.lsapp.smarthome.data.base.** {*;}
-keep public class com.lsapp.smarthome.data.** {*;}
-keep public class com.lsapp.smarthome.app.** {*;}
-keep public class com.lsapp.smarthome.event.** {*;}
#-keep public class com.lsapp.smarthome.hiflying.** {*;}

#onclick
-keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
}

#pgyer
#-libraryjars libs/pgyer_sdk_x.x.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }

#alicloud
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**

#share sdk
-keep class cn.sharesdk.**{*;}
-keep class cn.smssdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.sharesdk.wechat.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#amap location
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#mpchart
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

#okhttp retrofit
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit2.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#shut up
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn com.lsapp.smarthome.**
-dontwarn org.codehaus.mojo.**


########--------Retrofit + RxJava--------#########
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn sun.misc.Unsafe
-dontwarn com.octo.android.robospice.retrofit.RetrofitJackson**
-dontwarn retrofit.appengine.UrlFetchClient
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit.**

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}
# ALSO REMEMBER KEEPING YOUR MODEL CLASSES
-keep class com.your.package.model.** { *; }

#youmeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}


