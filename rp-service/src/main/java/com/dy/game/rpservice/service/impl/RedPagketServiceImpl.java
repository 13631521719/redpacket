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
import javax.persistence.Basic;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(isolation= Isolation.SERIALIZABLE)
    public WebResponse sendRedPagket(SendRedPagketParam param) throws Exception {
        //1.验证参数的有效性，参考SendRedPagketParam中的注解
        //2.判断红包金额是否超额，这里只处理群发红包
        if (param.getMoney()>(param.getNum()*200))
            throw new BaseException("单个红包金额不可超过200元");
        //3.判断用户余额是否充裕
        UserEntity user=userService.findByUserCode(param.getUserCode());
        if (user==null)
            throw new BaseException("错误的用户编码，对应的用户信息不存在");
        if (user.getBalance()<param.getMoney())
            throw new BaseException("可用余额不足");
        //4.在用户余额中减去红包的金额
        Double balance=new BigDecimal(user.getBalance()).subtract(new BigDecimal(param.getMoney())).doubleValue();//防止丢精度
        user.setBalance(balance);
        //保存修改后的用户余额
        userService.save(user);
        //5.分配每个红包的金额，并存入redis的list
        RedPagketEntity redPagketEntity=createdRedpagketEntity(param);//创建红包对象
        //6.缓存红包信息
        redPagketRepository.save(redPagketEntity);//存入数据库
        RedisTemplate.set(redPagketEntity.getRedPagketCode(),redPagketEntity);//存入缓存
        return null;
    }

    /**
     * 抢红包
     * @param grabRedPagketParam
     * @return
     * @throws Exception
     */
    @Override
    public WebResponse gradRedPagket(GrabRedPagketParam grabRedPagketParam) throws Exception {
        return null;
    }

    /**
     * 创建红包entity对象
     * @param p
     * @return
     * @throws Exception
     */
    private RedPagketEntity createdRedpagketEntity(SendRedPagketParam p)throws Exception{
        RedPagketEntity e=new RedPagketEntity();
        BeanUtils.copyProperties(p,e);
        e.setCoverUrl(StringUtils.isBlank(p.getCoverUrl())?p.getCoverUrl():RedPagketConstant.DEFAULT_COVER_URL);//红包封面
        e.setMessage(StringUtils.isBlank(p.getMessage())?p.getMessage():RedPagketConstant.DEFAULT_MESSAGE);//红包提示语
        e.setRedPagketCode(UUID.randomUUID().toString());//红包编码
        e.setSendTime(new Date());//发送时间
        e.setEndTime(new Date(e.getSendTime().getTime()+RedPagketConstant.EXPIRE_TIME));//过期时间
        e.setBalance(p.getMoney());//红包余额
        e.setGrabedNum(0);//已抢红包数
        e.setStatus(RedPagketConstant.STATUS_GRABING);//红包状态
        return e;
    }

    /**
     * 预分配每个用户可以抢到的金额
     * @param e
     * @return
     * @throws Exception
     */
    private List<RedPagketChildEntity> allocationMoney(RedPagketEntity e)throws Exception{
        List<RedPagketChildEntity> rpKids=new ArrayList<>(e.getNum());//创建集合保存子红包（用户具体抢到的红包）信息
        BigDecimal money=new BigDecimal(e.getMoney());
        BigDecimal avgMoney=money.divide(BigDecimal.valueOf(3),2,BigDecimal.ROUND_HALF_DOWN);//计算红包的平均金额，保留两位小数
        //n-1个红包分配平均金额后的剩余金额
        BigDecimal residue=money.subtract(//减
                avgMoney.multiply(BigDecimal.valueOf(e.getGrabedNum()-1))//前n-1个金额的平均金额之和
        );
        //如果红包数量是奇数
        if (e.getNum()%2>0){
            //创建子红包对象
            RedPagketChildEntity childEntity=createdRPKidsEntity(e.getRedPagketCode(),residue.doubleValue());
            rpKids.add(childEntity);
        }else {

        }
        //如果红包数量是偶数
        return rpKids;
    }

    /**
     * 创建子红包对象
     * @return
     * @throws Exception
     */
    private RedPagketChildEntity createdRPKidsEntity(String redPagketCode,Double money)throws Exception{
        RedPagketChildEntity e=new RedPagketChildEntity();
        e.setRedPagketCode(redPagketCode);//红包编码
        e.setMoney(money);//红包金额
        e.setStatus(RPKidsConstant.NOT);//红包状态
        return e;
    }

    /**
     * 获取随机double类型的随机数
     * @param MAX
     * @return
     */
    public static Double queryHongBao(double MAX) {
        Random rand = new Random();
        double MIN=0.01;
        double result=0;
        for(int i=0; i<10; i++){
            result = MIN + (rand.nextDouble() * (MAX - MIN));
            result = (double) Math.round(result * 100) / 100;
        }
        return result;
    }

    public static void main(String[] args) {
        BigDecimal money=new BigDecimal(1.17);
        int i=3;

        System.out.println(i%2);
    }
}
