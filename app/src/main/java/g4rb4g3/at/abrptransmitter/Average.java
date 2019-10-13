package g4rb4g3.at.abrptransmitter;

public class Average {

    private static long intervalStart = 0;
    private static long lastTime = 0;
    private static float lastConsumption = 0;
    private static float consumptionSum = 0;

    public static void addValueToAverageConsumption(float kw) {
        long currentTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = currentTime;
            lastConsumption = kw;
            consumptionSum = 0;
            intervalStart = currentTime;
            return;
        }
        consumptionSum += lastConsumption * (currentTime - lastTime);
        lastTime = currentTime;
        lastConsumption = kw;
    }

    public static float getAverageConsumption(long updateInterval) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - intervalStart > updateInterval + 500) {
            // sometimes, especially when switching the car to P, it may happen, that the timer
            // is called later than it should be called. as this value might be way off we
            // should skip it

            lastTime = 0;
            return -1;
        }

        consumptionSum += lastConsumption * (currentTime - lastTime);
        lastTime = 0;
        return consumptionSum / updateInterval;
    }
}
