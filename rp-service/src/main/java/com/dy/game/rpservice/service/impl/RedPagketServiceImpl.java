package com.dy.game.rpservice.service.impl;

import com.dy.game.rpcommon.Template.RedisTemplate;
import com.dy.game.rpcommon.constant.RPKidsConstant;
import com.dy.game.rpcommon.constant.RedPagketConstant;
import com.dy.game.rpcommon.entity.RedPagketChildEntity;
import com.dy.game.rpcommon.entity.RedPagketEntity;
import com.dy.game.rpcommon.entity.UserEntity;
import com.dy.game.rpcommon.param.GrabRedPagketParam;
import com.dy.game.rpcommon.param.SendRedPagketParam;
import com.dy.game.rpcommon.repository.RedPagketRepository;
import com.dy.game.rpcommon.response.ResponseUtil;
import com.dy.game.rpcommon.response.WebResponse;
import com.dy.game.rpservice.exception.BaseException;
import com.dy.game.rpservice.service.RedPagketService;
import com.dy.game.rpservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @作者 liulin
 * @描述 操作红包的service
 */
@Service
public class RedPagketServiceImpl implements RedPagketService {
    @Resource
    private RedPagketRepository redPagketRepository;
    @Resource
    private UserService userService;

    /**
     * 群发红包
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public WebResponse sendRedPagket(SendRedPagketParam param) throws Exception {
        //1.验证参数的有效性，参考SendRedPagketParam中的注解
        //2.判断红包金额是否超额，这里只处理群发红包
        if (param.getMoney() > (param.getNum() * 200))
            throw new BaseException("单个红包金额不可超过200元");
        //3.判断用户余额是否充裕
        UserEntity user = userService.findByUserCode(param.getUserCode());
        if (user == null)
            throw new BaseException("错误的用户编码，对应的用户信息不存在");
        if (user.getBalance() < param.getMoney())
            throw new BaseException("可用余额不足");
        //4.在用户余额中减去红包的金额
        Double balance = new BigDecimal(user.getBalance()).subtract(new BigDecimal(param.getMoney())).doubleValue();//防止丢精度
        user.setBalance(balance);
        //保存修改后的用户余额
        userService.save(user);
        //5.分配每个红包的金额，并存入redis的list
        RedPagketEntity redPagketEntity = this.createdRedpagketEntity(param);//创建母红包对象
        List<RedPagketChildEntity> kidRedPagkets = this.allocationMoney(redPagketEntity);//分配每个子红包的额度
        RedisTemplate.lRightPushAll(RedPagketConstant.createrListKey(redPagketEntity.getRedPagketCode()),
                Collections.singleton(kidRedPagkets));//存入redis
        //6.保存母红包信息
        redPagketRepository.save(redPagketEntity);//存入数据库
        RedisTemplate.set(redPagketEntity.getRedPagketCode(), redPagketEntity);//存入缓存
        return ResponseUtil.success();
    }

    /**
     * 抢红包
     *
     * @param p
     * @return
     * @throws Exception
     */
    @Override
    public WebResponse gradRedPagket(GrabRedPagketParam p) throws Exception {
        //第一步：判断红包是否被抢完
        this.checkOver(p.getRedPagketCode(),false);
        //第二步：获取锁
        this.getLock(p.getRedPagketCode());
        //第三步：再次判断红包是否被抢完
        RedPagketEntity rp = this.checkOver(p.getRedPagketCode(),true);
        // 获取自己的红包金额，并加入到用户余额再存入数据库，记录抢红包信息到redis
        double redPagketMoney = this.operationRedPagket(rp.getGrabedNum(), p);
        // 已抢红包数加一，
        rp.setGrabedNum(rp.getGrabedNum() + 1);
        //在红包余额中减去当前红包的金额
        rp.setBalance(BigDecimal.valueOf(rp.getBalance()).subtract(BigDecimal.valueOf(redPagketMoney)).doubleValue());
        //存入redis
        RedisTemplate.set(p.getGroupCode(),rp);
        //第四步：释放锁
        RedisTemplate.deleteLock(RedPagketConstant.createrLockKey(p.getRedPagketCode()));
        //返回抢到的红包金额
        return ResponseUtil.success(redPagketMoney);
    }

    /**
     * 获取自己的红包金额，已抢红包数加一，记录抢红包信息到redis
     *
     * @param grabedNum 已抢红包数
     * @param p         抢红包的参数对象
     * @return
     * @throws Exception
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public double operationRedPagket(int grabedNum, GrabRedPagketParam p) throws Exception {
        //获取预先分配的各个红包的金额
        List<Object> kidRedPagkets = RedisTemplate.lRangeWithObj(RedPagketConstant.createrListKey(p.getRedPagketCode()), 0, -1);
        //获取当前用户应得的红包
        RedPagketChildEntity redPagketChild = (RedPagketChildEntity) kidRedPagkets.get(grabedNum);
        //获取该红包的金额
        double redPagketMoney = redPagketChild.getMoney();
        //获取当前用户信息
        UserEntity user = userService.findByUserCode(p.getUserCode());
        redPagketChild.setStatus(RPKidsConstant.GOT);//设置红包状态为"已抢"
        redPagketChild.setUserCode(p.getUserCode());//设置用户编码到红包信息中
        redPagketChild.setGotTime(new Date());//设置抢红包的时间
        //将红包金额加入用户余额
        user.setBalance(BigDecimal.valueOf(user.getBalance()).add(BigDecimal.valueOf(redPagketMoney)).doubleValue());
        //入库
        userService.save(user);
        //记录抢红包信息到redis
        kidRedPagkets.set(grabedNum, redPagketChild);
        RedisTemplate.lRightPushAll(RedPagketConstant.createrListKey(p.getRedPagketCode()),
                Collections.singleton(kidRedPagkets));//存入redis
        return redPagketMoney;
    }

    /**
     * 获取锁
     *
     * @param pagketCode
     * @throws Exception
     */
    private void getLock(String pagketCode) throws Exception {
        //创建锁的key
        String lockKey = RedPagketConstant.createrLockKey(pagketCode);
        //获取分布式锁
        int getNum = 0;//获取锁的次数
        boolean LOCK = false;
        do {
            LOCK = RedisTemplate.lock(lockKey);
            if (LOCK)
                break;
            getNum++;
        } while (getNum < 5);
        if (!LOCK)
            throw new BaseException("人气太旺了，请稍后再试");
    }

    /**
     * 判断红包是否被抢完、或超时
     *
     * @param redPagketCode 红包编码
     * @param flag          true ：释放锁，false：不释放
     * @return
     * @throws Exception
     */
    private RedPagketEntity checkOver(String redPagketCode, boolean flag) throws Exception {
        Object o = RedisTemplate.get(redPagketCode);
        if (o == null) {
            if (flag)
                RedisTemplate.deleteLock(RedPagketConstant.createrLockKey(redPagketCode));//释放锁
            throw new BaseException("红包超时未领取");
        }

        RedPagketEntity entity = (RedPagketEntity) o;
        if (entity.getNum() == entity.getGrabedNum()) {//判断红包是否被抢完
            if (flag)
                RedisTemplate.deleteLock(RedPagketConstant.createrLockKey(redPagketCode));//释放锁
            throw new BaseException("手慢了，红包已派发完毕");
        }

        return entity;
    }

    /**
     * 创建红包entity对象
     *
     * @param p
     * @return
     * @throws Exception
     */
    private RedPagketEntity createdRedpagketEntity(SendRedPagketParam p) throws Exception {
        RedPagketEntity e = new RedPagketEntity();
        BeanUtils.copyProperties(p, e);
        e.setCoverUrl(StringUtils.isBlank(p.getCoverUrl()) ? p.getCoverUrl() : RedPagketConstant.DEFAULT_COVER_URL);//红包封面
        e.setMessage(StringUtils.isBlank(p.getMessage()) ? p.getMessage() : RedPagketConstant.DEFAULT_MESSAGE);//红包提示语
        e.setRedPagketCode(UUID.randomUUID().toString());//红包编码
        e.setSendTime(new Date());//发送时间
        e.setEndTime(new Date(e.getSendTime().getTime() + RedPagketConstant.EXPIRE_TIME));//过期时间
        e.setBalance(p.getMoney());//红包余额
        e.setGrabedNum(0);//已抢红包数
        e.setStatus(RedPagketConstant.STATUS_GRABING);//红包状态
        return e;
    }

    /**
     * 预分配每个用户可以抢到的金额
     *
     * @param e
     * @return
     * @throws Exception
     */
    private List<RedPagketChildEntity> allocationMoney(RedPagketEntity e) throws Exception {
        List<RedPagketChildEntity> rpKids = null;//创建集合保存子红包（用户具体抢到的红包）信息
        BigDecimal money = new BigDecimal(e.getMoney());
        BigDecimal avgMoney = money.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_DOWN);//计算红包的平均金额，保留两位小数
        //n-1个红包分配平均金额后的剩余金额
        BigDecimal residue = money.subtract(//减
                avgMoney.multiply(BigDecimal.valueOf(e.getGrabedNum() - 1))//前n-1个金额的平均金额之和
        );
        //如果红包数量是奇数
        if (e.getNum() % 2 > 0) {
            //创建子红包集合
            rpKids = this.createdRPKidsEntity(e.getRedPagketCode(), avgMoney.doubleValue(), e.getNum() - 1);
            //创建子红包对象
            RedPagketChildEntity childEntity = this.createdRPKidsEntity(e.getRedPagketCode(), residue.doubleValue());
            rpKids.add(childEntity);
        } else {//如果红包数量是偶数
            rpKids = this.createdRPKidsEntity(e.getRedPagketCode(), avgMoney.doubleValue(), e.getNum());
        }
        return rpKids;
    }

    /**
     * 创建子红包对象
     *
     * @return
     * @throws Exception
     */
    private RedPagketChildEntity createdRPKidsEntity(String redPagketCode, Double money) throws Exception {
        RedPagketChildEntity e = new RedPagketChildEntity();
        e.setRedPagketCode(redPagketCode);//红包编码
        e.setMoney(money);//红包金额
        e.setStatus(RPKidsConstant.NOT);//红包状态
        return e;
    }

    /**
     * 创建子红包集合
     *
     * @param redPagketCode 红包编码
     * @param avgMoney      红包平均额度
     * @param index         红包数（偶数）
     * @return
     * @throws Exception
     */
    private List<RedPagketChildEntity> createdRPKidsEntity(String redPagketCode, Double avgMoney, int index) throws Exception {
        List<RedPagketChildEntity> entities = new ArrayList<>();
        /**
         * 一次性分配两个红包的额度，这两个红包的额度之和等于平均额度乘以2
         * 通过遍历分配完偶数个红包
         */
        for (int t = 0; t < index; t = t + 2) {
            Double redPagketMoney1 = queryHongBao(avgMoney);//获取一个不大于avgMoney的红包金额
            BigDecimal avgMoneyDouble = BigDecimal.valueOf(avgMoney).multiply(BigDecimal.valueOf(2));//平均值乘以2
            double redPagketMoney2 = avgMoneyDouble.subtract(BigDecimal.valueOf(redPagketMoney1)).doubleValue();//计算另一个红包的额度
            //判断maxMoney或者平均值乘以2减maxMoney是否大于200
            if (redPagketMoney1 > RedPagketConstant.MAX_LIMIT
                    || redPagketMoney2 > RedPagketConstant.MAX_LIMIT) {
                redPagketMoney1 = RedPagketConstant.MAX_LIMIT;//红包1的额度
                redPagketMoney2 = avgMoneyDouble.subtract(BigDecimal.valueOf(RedPagketConstant.MAX_LIMIT)).doubleValue();//红包2的额度
            }
            RedPagketChildEntity entity1 = createdRPKidsEntity(redPagketCode, redPagketMoney1);//创建红包对象1
            RedPagketChildEntity entity2 = createdRPKidsEntity(redPagketCode, redPagketMoney2);//创建红包对象2
            entities.add(entity1);
            entities.add(entity2);
        }
        return entities;
    }

    /**
     * 获取指定最大值的double类型的随机数
     *
     * @param MAX 最大值
     * @return
     */
    public static Double queryHongBao(double MAX) {
        Random rand = new Random();
        double MIN = 0.01;
        double result = 0;
        for (int i = 0; i < 10; i++) {
            result = MIN + (rand.nextDouble() * (MAX - MIN));
            result = (double) Math.round(result * 100) / 100;
        }
        return result;
    }

    public static void main(String[] args) {
        BigDecimal money = new BigDecimal(1.17);
        int i = 3;

        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
        System.out.println(queryHongBao(3.55));
    }
}
