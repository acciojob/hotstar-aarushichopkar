package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        if(userRepository.findById(userId).isPresent()){
            User user = userRepository.findById(userId).get();
            Integer userAge = user.getAge();
            Subscription userSubs = user.getSubscription();
            List<WebSeries> availableWebSeries = webSeriesRepository.findAllByAgeLimitLessThanEqual(userAge);
            int basicPlanCount=0;
            int proPlanCount=0;
            int elitePlanCount=0;

            for(WebSeries ws: availableWebSeries){
                if(ws.getSubscriptionType()==SubscriptionType.BASIC){
                    basicPlanCount++;
                } else if (ws.getSubscriptionType()==SubscriptionType.PRO) {
                    proPlanCount++;
                }
                else{
                    elitePlanCount++;
                }
            }
            if(user.getSubscription().getSubscriptionType()==SubscriptionType.BASIC){
                return basicPlanCount;
            } else if (user.getSubscription().getSubscriptionType()==SubscriptionType.PRO) {
                return basicPlanCount+proPlanCount;
            }
            else {
                return basicPlanCount+proPlanCount+elitePlanCount;
            }
        }
        else{
            throw new NullPointerException("User not Present");
        }

    }
}
