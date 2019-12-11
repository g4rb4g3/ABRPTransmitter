package g4rb4g3.at.abrptransmitter;

public class Average {

    private long intervalStart = 0;
    private long lastTime = 0;
    private float lastConsumption = 0;
    private float consumptionSum = 0;

    public void addValueToAverageConsumption(float kw) {
        long currentTime = System.currentTimeMillis();
        if (this.lastTime == 0) {
            this.lastTime = currentTime;
            this.lastConsumption = kw;
            this.consumptionSum = 0;
            this.intervalStart = currentTime;
            return;
        }
        this.consumptionSum += this.lastConsumption * (currentTime - this.lastTime);
        this.lastTime = currentTime;
        this.lastConsumption = kw;
    }

    public float getAverageConsumption() {
        long currentTime = System.currentTimeMillis();
        this.consumptionSum += this.lastConsumption * (currentTime - this.lastTime);
        this.lastTime = 0;
        // protect against division by zero
        if ((currentTime - this.intervalStart)>0) {
            return this.consumptionSum / (currentTime - this.intervalStart);
        }
        else {
            return 0;
        }
    }
}
