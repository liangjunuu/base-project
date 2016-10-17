package com.base.web.index.controller;

import com.base.service.IndexService;
import com.base.support.redis.RedisClientTemplate;
import com.base.util.RedisConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjun on 16/7/2.
 */
@Controller
@RequestMapping("/index/")
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Autowired
    private IndexService indexService;

    @RequestMapping(value="index", method= RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,HttpServletResponse response) throws Exception{
        ModelAndView mav = new ModelAndView();
        int count = indexService.getIndexCount();
        logger.debug("====="+count);
        mav.addObject("count",count);
        redisClientTemplate.set(RedisConstantsUtil.SET_KEY,"123456789");
        logger.debug("=="+redisClientTemplate.get(RedisConstantsUtil.SET_KEY));
        redisClientTemplate.lpush(RedisConstantsUtil.LIST_KEY,"list1");
        redisClientTemplate.lpush(RedisConstantsUtil.LIST_KEY,"list2");
        redisClientTemplate.lpush(RedisConstantsUtil.LIST_KEY,"list2");
        mav.addObject("list",redisClientTemplate.lrange(RedisConstantsUtil.LIST_KEY, 0, redisClientTemplate.llen(RedisConstantsUtil.LIST_KEY)));

        Map<String,String> map = new HashMap<String, String>();
        map.put("a","11");
        map.put("b","22");
        redisClientTemplate.hmset(RedisConstantsUtil.HASHSET_KEY, map);
        mav.addObject("map",redisClientTemplate.hgetAll(RedisConstantsUtil.HASHSET_KEY));
        return mav;
    }

    @ResponseBody
    @RequestMapping(value="getIndex", method= RequestMethod.GET)
    public Object getIndex(HttpServletRequest request,HttpServletResponse response) throws Exception{
        if(null == request.getHeader("abc") || "".equals(request.getHeader("abc"))) {
            request.setAttribute("abc", "1234567890");
            response.setHeader("abc","112233");
            return "fail";
        }
        return "sucess";
    }
}
