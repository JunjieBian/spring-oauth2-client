package com.adley.oauth.client.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ResourceUtils;

import com.adley.oauth.client.modal.SpsUserDetails;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		try {
			Map<String, SpsUserDetails> spsUserDetailsHashMap = new HashMap();
			JSONObject.parseArray(String.join("", Files.readAllLines(ResourceUtils.getFile("classpath:auth.json").toPath())), SpsUserDetails.class).forEach(s -> {
				spsUserDetailsHashMap.put(s.getUsername(), s);
			});
			if (null == spsUserDetailsHashMap.get(userName)) {
				return new SpsUserDetails();
			} else {
				return spsUserDetailsHashMap.get(userName);
			}
		} catch (IOException e) {
			log.error("ERROR", e);
			return new SpsUserDetails();
		}
	}
}