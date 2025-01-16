package com.example.backend.user.service;

import com.example.backend.user.model.entity.User;
import com.example.backend.user.model.entity.UserAffiliation;
import com.example.backend.user.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser() {
        User user = new User();
        user.setName("이예림");
        user.setEmail("1117mg@sookmyung.ac.kr");

        UserAffiliation affiliation1 = new UserAffiliation();
        affiliation1.setAffiliation("숙명여자대학교");
        affiliation1.setNumber("2015869");
        affiliation1.setUser(user);

        UserAffiliation affiliation2 = new UserAffiliation();
        affiliation2.setAffiliation("ABC Company");
        affiliation2.setNumber("EMP12345");
        affiliation2.setUser(user);

        user.setAffiliations(Arrays.asList(affiliation1, affiliation2));
        userRepository.save(user);
    }
}