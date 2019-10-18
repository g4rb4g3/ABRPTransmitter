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

    public static float getAverageConsumption() {
        long currentTime = System.currentTimeMillis();
        consumptionSum += lastConsumption * (currentTime - lastTime);
        lastTime = 0;
        return consumptionSum / (currentTime - intervalStart);
    }
}
