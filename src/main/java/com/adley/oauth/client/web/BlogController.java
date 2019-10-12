package com.adley.oauth.client.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET, value="/list")
    public ModelAndView blogList(@CookieValue(name="authToken") String authToken, ModelAndView modelAndView, HttpServletResponse response) {
        try {
            assert StringUtils.hasText(authToken);
            response.addCookie(new Cookie("authToken",authToken));
            modelAndView.setViewName("blogs/blogs");
            modelAndView.addObject("blogList", JSONObject.parseArray(getResource("/blogs/blog/list",authToken),JSONObject.class));
        } catch(Exception e) {
            log.error("请求异常",e);
            modelAndView.setViewName("/api_error");
        }
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value="/content", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String readBlogs(String blogId, @CookieValue(name="authToken") String authToken) {
        try {
            return getResource("/blogs/blog/"+blogId,authToken);
        } catch(Exception e) {
            log.error("请求异常",e);
            return "";
        }
    }

    private String getResource(String url, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer "+authToken);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(env.getProperty("auth.resource.server.url")+url, request, String.class);
        return responseEntity.getBody();
    }
}
