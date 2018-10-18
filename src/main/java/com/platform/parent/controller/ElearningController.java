package com.platform.parent.controller;

import com.platform.parent.config.GroupPurchaseConfig;
import com.platform.parent.mybatis.bean.GroupPurchase;
import com.platform.parent.mybatis.dao.CourseMapper;
import com.platform.parent.mybatis.service.CourseService;
import com.platform.parent.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by dengb.
 */
@RestController
@RequestMapping(value = "/elearning")
public class ElearningController {
    private static final Logger logger = LoggerFactory.getLogger(ElearningController.class);

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GroupPurchaseConfig gpConfig;

//    @RequestMapping(value = {
//            "",
//            "/*",
//            "**"
//    })
    public @ResponseBody
    String elearning(HttpServletRequest request, HttpServletResponse response) {
        String unionid = request.getParameter("id");
        String path = request.getServletPath();
        List<GroupPurchase> gps = courseMapper.findGroupPurchaseByUnionid(unionid);
        logger.info("In elearning, gps: {}", gps);
        logger.info("getContextPath : {}, getServletPath: {}, getPathInfo: {}, getRequestURL: {}, getRequestURI: {}",
                request.getContextPath(), request.getServletPath(), request.getPathInfo(), request.getRequestURL(), request.getRequestURI());
        if (gps == null || gps.size() == 0)
        {
            return "您尚未购买课程！";
//            try
//            {
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType("text/html; charset=UTF-8");
//                response.getWriter().println("您尚未购买课程！");
//            }
//            catch (Exception exception)
//            {
//                exception.printStackTrace();
//            }
//
//            return null;
        }

        String servletPrefix = "/elearning";
        String uri = request.getServletPath();
        logger.info("In elearning, uri: {}", uri);
        int prefixIndex = uri.indexOf(servletPrefix, 0);
        logger.info("In elearning, prefixIndex: {}", prefixIndex);
        uri = uri.substring(prefixIndex + servletPrefix.length());
        logger.info("In elearning, uri: {}", uri);
        uri = "/hehe" + uri;
        logger.info("In elearning, fwd to uri: {}", uri);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");//uri);
        logger.info("In elearning, got dispatcher");
        try
        {
//            response.reset();
//            dispatcher.forward(request,response);
//            request.getRequestDispatcher("/index.html").forward(request,response);
            request.getRequestDispatcher(uri).forward(request,response);
            logger.info("In elearning, dispatcher forwarded");
            return null;
        }
        catch (Exception exception)
        {
            logger.info("In elearning, dispatcher forwarded exception");
            exception.printStackTrace();
        }

        logger.info("In elearning, dispatcher forwarded done, return");
        return null;
//        dispatcher.ge
//
//        response.getOutputStream().
//        return "forwarded";
    }

    @RequestMapping(value = {
            "",
            "/*",
            "**"
    })
    public @ResponseBody
    String elearning1(HttpServletRequest request, HttpServletResponse response) {
        String authURL = "http://www.mxjzq.com/mingxiao/user/info";
        try
        {
//            String result = HttpClientUtil.CreatePostHttpConnection(authURL);
            logger.info("In elearning connecting to authURL: {}.", authURL);
            HttpURLConnection conn = HttpClientUtil.CreatePostHttpConnection(authURL);
            logger.info("In elearning done connecting to authURL: {} and returned reponse code {} " +
                            "and response messaage {} and content {}.", authURL
                        ,conn.getResponseCode(), conn.getResponseMessage());
            InputStream input = null;

            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            String result = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("In elearning got result {}.", result);
        }
        catch (Exception exception)
        {
            logger.info("In elearning got exception {}.", exception.getMessage());
            exception.printStackTrace();
        }

        String unionid = request.getParameter("id");
        String path = request.getServletPath();
        List<GroupPurchase> gps = courseMapper.findGroupPurchaseByUnionid(unionid);
        logger.info("In elearning, gps: {}", gps);
        logger.info("getContextPath : {}, getServletPath: {}, getPathInfo: {}, getRequestURL: {}, getRequestURI: {}",
                request.getContextPath(), request.getServletPath(), request.getPathInfo(), request.getRequestURL(), request.getRequestURI());
        if (gps == null || gps.size() == 0)
        {
            return "您尚未购买课程！";
        }

        String servletPrefix = "/elearning";
        String uri = request.getServletPath();
        logger.info("In elearning, uri: {}", uri);
        int prefixIndex = uri.indexOf(servletPrefix, 0);
        logger.info("In elearning, prefixIndex: {}", prefixIndex);
        uri = uri.substring(prefixIndex + servletPrefix.length());
        logger.info("In elearning, uri: {}", uri);
        uri = "/hehe" + uri;
        logger.info("In elearning, fwd to uri: {}", uri);
        logger.info("In elearning, got dispatcher");
        try
        {
            request.getRequestDispatcher(uri).forward(request,response);
            logger.info("In elearning, dispatcher forwarded");
            return null;
        }
        catch (Exception exception)
        {
            logger.info("In elearning, dispatcher forwarded exception");
            exception.printStackTrace();
        }

        logger.info("In elearning, dispatcher forwarded done, return");
        return null;
    }
}
