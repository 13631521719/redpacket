package com.dy.game.rpcommon.constant;



public class RedPagketConstant {
    //红包有效期
    public static final int EXPIRE_TIME=24*60*60*1000;
    //红包默认提示语
    public static final String DEFAULT_MESSAGE="恭喜发财，大吉大利";
    //红包默认封面地址
    public static final String DEFAULT_COVER_URL="https://www.XXX.PNG";
    //红包状态：争抢中
    public static final String STATUS_GRABING="争抢中";
    //红包状态：已抢完
    public static final String STATUS_FINISH="已抢完";
    //红包状态：已过期
    public static final String STATUS_TIMEOUT="已过期";
    //红包的最大值
    public static final double MAX_LIMIT=200;
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 300;

    /**
     * 创建红包List存入REDIS的key
     * @param redPagketCode
     * @return
     */
    public static String createrListKey(String redPagketCode){
        return "CHILD_RED_PAGKET_LIST_"+redPagketCode;
    }
    /**
     * 创建红包List存入REDIS的key
     * @param redPagketCode
     * @return
     */
    public static String createrLockKey(String redPagketCode){
        return "CHILD_RED_PAGKET_LOCK_"+redPagketCode;
    }
}
