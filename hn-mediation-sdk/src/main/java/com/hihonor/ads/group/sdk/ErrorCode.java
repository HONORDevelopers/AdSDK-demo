package com.hihonor.ads.group.sdk;



public interface ErrorCode {
    //注意！！！！端侧错误需要从100000开始。避开后面定义的错误码：1~10000 通用错误、10001~20000 应用市场错误、20001~30000 智慧服务错误

    /**
     * 请求成功
     */
    int SUCCESS = 0;

    /**
     * 初始化错误/失败 包含配置值为空、网络请求失败等
     */
    int INIT_ERROR = 30001;

    /**
     * 未初始化，需要先初始化后再请求广告
     */
    int NOT_INIT = 30002;

    /**
     * 没有网络连接
     */
    int NOT_NETWORK = 1012;

    /**
     * 广告AdSlot对象为空
     */
    int AD_SLOT_NONE = 30003;

    /**
     * 广告slotId为空
     */
    int AD_SLOT_ID_EMPTY = 30004;

    /**
     * 响应数据为空
     */
    int RESPONSE_DATA_EMPTY = 30005;

    /**
     * 响应失败
     */
    int RESPONSE_FAIL = 30006;

    /**
     * 媒体广告状态为关闭状态
     */
    int AD_MEDIA_DISABLE = 30007;

    /**
     * appKey为空
     */
    int AD_APP_KEY_EMPTY = 30008;

    /**
     * 上报广告view加载超时
     */
    int HI_AD_VIEW_ADD_TIME_OUT = 30009;
    String REPORT_AD_VIEW_ADD_TIME_OUT_MSG = "Ad rendered, but add page timeout.";

    /**
     * 上报广告类型不匹配
     */
    int HI_AD_TYPE_MISMATCH = 30010;
    String REPORT_AD_TYPE_MISMATCH = "AD type mismatch";

    /**
     * 上报广告-已安装应用列表全被过滤
     */
    int HI_AD_PACK_INSTALLED = 30011;
    String REPORT_AD_PACK_HI_AD_PACK_INSTALLED = "AD filter by package installed";

    /**
     * 广告被端侧过滤掉了
     */
    int HI_AD_EMPTY_BY_FILTER = 30012;
    String REPORT_AD_EMPTY_BY_FILTER = "AD filter by SDK, ad list is empty.";

    /**
     * 广告加载超时，这里超时指超出媒体在{@link AdSlot#getTimeOutMillis()}设置的超时时间阈值，单位：ms。
     * 例如：媒体设置了1.5s超时时间就需要设置1500，当广告加载流程到回调给媒体成功前的事件总合超过了1500ms则
     * 被判定为超时，广告sdk将放弃将广告数据返回给媒体，并上报广告及广告位曝光失败埋点。
     */
    int AD_LOADING_TIME_OUT = 30013;

    /**
     * 当后台返回的广告多余媒体设置的数量
     */
    int AD_LOADING_EXCESS_EXPOSURE = 30014;
    String EXCESS_EXPOSURE_MESSAGE = "The backend returns an ad that exceeds the media settings";

    /**
     * 安装失败-预装应用
     */
    int AD_INSTALL_FAIL = 30015;
    String REPORT_AD_INSTALL_FAIL = "AD install fail by Pre-installed apps";

    /**
     * 下载失败
     */
    int AD_DOWN_LOAD_FAIL = 30016;
    String REPORT_AD_DOWN_LOAD_FAIL = "AD pack app down fail.";

    /**
     * 下载开始失败
     */
    int AD_DOWN_LOAD_START_FAIL = 30017;
    String REPORT_AD_DOWN_LOAD_START_FAIL = "AD pack app down start fail.";

    /**
     * 安装开始失败
     */
    int AD_INSTALL_START_FAIL = 30018;
    String REPORT_AD_INSTALL_START_FAIL = "AD install start fail.";

    /**
     * 安装下载时候广告数据是null
     */
    int AD_NULL_DOWN = 30019;
    String REPORT_AD_NULL_DOWN = "reportByStatus but ad is null.";

    /**
     * 端侧请求-端侧处理数据-返回媒体 时间链 code
     * AD_CLIENT_HANDLE_CHAIN_STA 媒体触发请求广告 （触发）
     * AD_CLIENT_HANDLE_CHAIN_REQ_RES 发起到接收 （网络传输时间）
     * AD_CLIENT_HANDLE_CHAIN_DATA 数据处理开始至完成 （端侧处理数据时间）
     * AD_CLIENT_HANDLE_CHAIN_CALL 数据处理完至返回给媒体时间 （数据返回至媒体）
     */
    int AD_CLIENT_HANDLE = 30020;
    String AD_CLIENT_HANDLE_REQ = "AD_CLIENT_HANDLE_REQ";
    String AD_CLIENT_HANDLE_DATA = "AD_CLIENT_HANDLE_DATA";
    String AD_CLIENT_HANDLE_CALL = "AD_CLIENT_HANDLE_CALL";

    /**
     * 下载取消
     */
    int AD_DOWN_LOAD_CANCEL = 30021;
    String REPORT_AD_DOWN_LOAD_CANCEL = "AD pack app down cancel.";

    /**
     * 打开广告详情页时候，webView出错
     */
    int AD_DEEP_LINK_WEB_VIEW_INIT_FAIL = 30022;
    String REPORT_AD_DEEP_LINK_WEB_VIEW_INIT_FAIL = "AD Deeplink initViews but  new WebView err ";

    /**
     * 用户开始下载/召回，但未实际开始下载
     */
    int AD_DOWN_LOAD_BUTTON_CLICK = 30023;
    String REPORT_AD_DOWN_LOAD_BUTTON_CLICK = "AD download or recover start";

    interface TRACK {
        /**
         * deeplink失败
         */
        int DEEPLINK_FAIL = 30024;
        String DEEPLINK_FAIL_MSG_START = "start deeplink fail";
        String DEEPLINK_FAIL_MSG_EMPTY = "deepLink is empty";
        /**
         * 请求不可达
         */
        int REQ_ERR = 30025;

        /**
         * 广告所在界面区域未展示
         */
        int AD_NOT_ADD_TO_PAGE = 30026;
        String AD_NOT_ADD_TO_PAGE_MSG = "Ad rendered,but on detached from window not add to page";


        /**
         * 物料加载失败
         */
        int AD_DATA_REQ_ERR = 30027;

        /**
         * view快速被移除
         */
        int AD_VIEW_REMOVE_FAST = 30028;

        /**
         * 更新器不支持
         */
        int AD_DOWN_LOAD_VERSION_SUPPORT_NO = 30029;
        String AD_NOT_SUPPORT_DOWN_LOAD_VERSION = "Ad filter data, but downloadInstallClient not support";

        /**
         * 预装应用 当前媒体是不具备调用权限
         */
        int AD_CURRENT_MEDIA_PERMISSIONS_NO = 30030;
        String AD_CURRENT_MEDIA_PERMISSIONS_NOT = "Ad filter data, but current media not has permissions";
    }

    /**
     * 更新器未创建
     */
    int AD_DOWN_CODE_CLIENT_IS_NULL = 30031;
    /**
     * 与更新器断开连接
     */
    int AD_DOWN_CODE_CLIENT_UN_CONNECTED = 30032;
    /**
     * 不在白名单内
     */
    int AD_DOWN_CODE_CLIENT_IS_NOT_WHITELIST = 30033;
    /**
     * 当前切换到流量网络，不支持流量网络下载的应用下载暂停
     */
    int AD_UNSUPPORT_MOBILE_NET_DOWNLOAD = 30034;
    /**
     * SDK-初始化时候 HnAdvertisingIdClient 为空
     */
    int AD_OAID_INFO_NULL = 30035;

    /**
     * 大数据埋点专用errorCode
     */
    interface TRACKING {
        /**
         * 上报tracking链接请求失败
         */
        int REPORT_FAIL = 100001;
        String REPORT_FAIL_MSG = "report fail";

        /**
         * 上报tracking链接前发生异常
         */
        int REPORT_BEFORE_EXCEPTION = 100002;
        String REPORT_FAIL_MSG_BEFORE_EXCEPTION = "report before fail";

        /**
         * 上报tracking链接为空
         */
        int URL_IS_EMPTY = 100003;
        String REPORT_FAIL_MSG_URL_EMPTY = "report url is empty";

        /**
         * 上报tracking失败次数最大
         */
        int REPORT_FAIL_MAX_LIMIT = 100004;
        String REPORT_FAIL_MSG_MAX_LIMIT = "report fail max limit";

        /**
         * 上报曝光大数据埋点过程异常
         */
        int HI_ANALYTICS_IMPRESSIONS_EXCEPTION = 100005;

        /**
         * 上报点击大数据埋点过程异常
         */
        int HI_ANALYTICS_CLICK_EXCEPTION = 100006;

        /**
         * 上报点击大数据埋点LandingPageUrl() is null
         */
        int HI_ANALYTICS_CLICK_LANDING_PAGE = 100007;
        String REPORT_FAIL_LANDING_PAGE_NULL = "get LandingPageUrl() is null when startWebDetailPage";
    }


}
