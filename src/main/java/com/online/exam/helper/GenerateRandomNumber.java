package com.online.exam.helper;



import java.util.List;

public class GenerateRandomNumber {

    public List<Long> generateRandomNumbers(int questionSize,int randomNumberLimit){
        int multiplier=generateMultiplierValue();
        Long seedValue=getSeedValue();
        int modulus=questionSize;
       return MultiplicativeCongruential.multiplicativeCongruentialMethod(seedValue,modulus,multiplier,randomNumberLimit);

    }
    public int generateMultiplierValue(){
        return 6;
    }
    public Long getSeedValue(){
        return System.currentTimeMillis();
    }

}
