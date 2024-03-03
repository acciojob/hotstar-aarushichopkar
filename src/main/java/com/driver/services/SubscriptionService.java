package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new java.util.Date());
        subscription.setUser(userRepository.findById(subscriptionEntryDto.getUserId()).get());

        int totalAmountPaid;
        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
            totalAmountPaid = 500+200*subscription.getNoOfScreensSubscribed();
        } else if (subscription.getSubscriptionType()==SubscriptionType.PRO) {
            totalAmountPaid = 800+250*subscription.getNoOfScreensSubscribed();
        }
        else {
            totalAmountPaid = 1000+350*subscription.getNoOfScreensSubscribed();
        }
        subscription.setTotalAmountPaid(totalAmountPaid);
        subscriptionRepository.save(subscription);
        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        Subscription subscription = subscriptionRepository.findByUserId(userId);

        int initailAmountPaid = subscription.getTotalAmountPaid();

        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(800+250*subscription.getNoOfScreensSubscribed());
        } else if (subscription.getSubscriptionType()==SubscriptionType.PRO) {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(1000+350*subscription.getNoOfScreensSubscribed());
        }
        else {
            throw new RuntimeException("Already the best Subscription");
        }
        subscriptionRepository.save(subscription);

        int finalAmountPaid = subscription.getTotalAmountPaid();
        return finalAmountPaid - initailAmountPaid;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription s: subscriptionList){
            totalRevenue+=s.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
