package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BodyParamService {

    @Autowired
    private BodyParamRepositoryImpl bodyParamRepository;

    public void saveBodyParam(BodyParam bodyParam) {
        bodyParamRepository.save(bodyParam);
    }

    public BodyParam findBodyParamByUser(User user) {
        return bodyParamRepository.findBodyParamByUser(user);
    }

}
