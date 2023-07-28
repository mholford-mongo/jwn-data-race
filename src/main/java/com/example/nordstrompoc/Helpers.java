package com.example.nordstrompoc;

public class Helpers {
    public static int getRandomInt(int low, int high)
    {
        java.util.Random r = new java.util.Random();
        int result = r.nextInt((high + 1) - low) + low;
        return result;
    }

    public static String getRandomString(int length) {
        return getRandomString(length, false);
    }

    public static String getRandomString(int length, boolean hexadecimalOnly) {
        String sampleSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        if (hexadecimalOnly == true)
        {
            sampleSet = "abcdef1234567890";
        }

        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.util.Random();

        while (sb.length() < length)
        {
            int index = (int) (rnd.nextFloat() * sampleSet.length());
            sb.append(sampleSet.charAt(index));
        }

        String results = sb.toString();
        return results;
    }

    public static java.util.Date getRandomDate(int plusMinusOffsetInSeconds)
    {
        // FOR USE WITH JAVA 8 DATE

        java.util.Random r = new java.util.Random();

        boolean negative = false;
        if(r.nextInt(100) <= (50))
        {
            negative = true;
        }

        r = new java.util.Random();
        int randomOffset = r.nextInt(plusMinusOffsetInSeconds + 1);

        java.util.Calendar calendar = java.util.Calendar.getInstance();

        if(negative){
            calendar.add(java.util.Calendar.SECOND, (randomOffset * -1));
        }
        else
        {
            calendar.add(java.util.Calendar.SECOND, (randomOffset));
        }

        return calendar.getTime();
    }

    public static boolean getRandomBoolean(double probabilityOfTrue)
    {
        // PROPERTY probabilityOfTrue CAN BE 0 TO 1 - expect .5 for even split
        // THIS CHANGES THE ODDS THAT THE RESULT WILL BE TRUE.
        java.util.Random r = new java.util.Random();
        int randomNumber = r.nextInt(100);

        if(randomNumber <= (100 * probabilityOfTrue))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static double getRandomDouble(double low, double high)
    {
        java.util.Random r = new java.util.Random();
        double result = (r.nextDouble() * (high - low)) + low;
        return result;
    }
}


