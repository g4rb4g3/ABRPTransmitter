package g4rb4g3.at.abrptransmitter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AverageUnitTest {

    @Test
    public void testAverageConsumption() {
        int updateInterval = 1000;
        Average Average = new Average();
        Average.addValueToAverageConsumption(5f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(10f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(5f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(10f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(7.5f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        assertEquals(7.5f, Average.getAverageConsumption(), 0.1);
    }

    @Test
    public void testAverageConsumption_only_one_value() {
        int updateInterval = 1000;
        Average Average = new Average();
        Average.addValueToAverageConsumption(5f);
        try {
            Thread.sleep(updateInterval);
        } catch (Exception e) {
        }
        assertEquals(5f, Average.getAverageConsumption(), 0.1);
    }

    @Test
    public void test_multiple_averages_result_in_different_average() {
        int updateInterval = 1000;
        Average Average = new Average();
        Average.addValueToAverageConsumption(1f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(2f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(3f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(4f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(5f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        assertEquals(3f, Average.getAverageConsumption(), 0.1);

        Average.addValueToAverageConsumption(6f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(6f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(6f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(6f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(6f);
        try {
            Thread.sleep(updateInterval / 5);
        } catch (Exception e) {
        }
        assertEquals(6f, Average.getAverageConsumption(), 0.1);
    }

    @Test
    public void test_timer_wrong_interval() {
        Average Average = new Average();
        Average.addValueToAverageConsumption(1);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(2);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(3);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(4);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(5);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(6);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Average.addValueToAverageConsumption(3.5f);
        try {
            Thread.sleep(400);
        } catch (Exception e) {
        }
        assertEquals(3.5, Average.getAverageConsumption(), 0.1);
    }
}