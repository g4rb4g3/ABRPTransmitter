package g4rb4g3.at.abrptransmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Utils {
  private static final Logger sLog = LoggerFactory.getLogger(Utils.class.getSimpleName());

  public static List<String> getIPAddresses() {
    List<String> ips = new ArrayList<>();
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            if (addr instanceof Inet4Address) {
              ips.add(addr.getHostAddress());
            } else {
              ips.add(addr.getHostAddress().substring(0, addr.getHostAddress().indexOf("%")));
            }
          }
        }
      }
    } catch (SocketException e) {
      sLog.error("error getting ip addresses", e);
    }
    return Collections.unmodifiableList(ips);
  }
  
  public static String getTimestamp() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date());
  }
}
