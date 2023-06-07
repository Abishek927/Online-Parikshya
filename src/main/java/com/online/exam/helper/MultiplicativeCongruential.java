package com.online.exam.helper;

import java.util.ArrayList;
import java.util.List;

public class MultiplicativeCongruential {
    //Function to generate random numbers
    public static List<Long> multiplicativeCongruentialMethod(Long seed, int modulus, int multiplier,int noOfRandomNums) {
        List<Long> numbers=new ArrayList<>();
        List<Long> result=new ArrayList<>();

        //Initialize the seed state
        numbers.add(0,seed);

        //Traverse to generate required numbers of random numbers
        for (int i=1;i<=noOfRandomNums;i++){
            //use the multiplicative congruential method
            numbers.add(i,(numbers.get(i-1)*multiplier)%modulus);
            result.add(i-1,numbers.get(i));
        }
        return result;
    }

}
