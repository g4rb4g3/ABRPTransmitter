package g4rb4g3.at.abrptransmitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.conn.util.InetAddressUtils;

public class Utils {
  public static List<String> getIPAddresses() throws SocketException {
    List<String> ips = new ArrayList<>();
    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
    for (NetworkInterface intf : interfaces) {
      List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
      for (InetAddress addr : addrs) {
        if (!addr.isLoopbackAddress()) {
          if (InetAddressUtils.isIPv4Address(addr.getHostAddress())) {
            ips.add(addr.getHostAddress());
          } else {
            ips.add(addr.getHostAddress().substring(0, addr.getHostAddress().indexOf("%")));
          }
        }
      }
    }
    return Collections.unmodifiableList(ips);
  }

  public static String getCurrentLogs(MainApplication mainApplication) throws IOException {
    StringBuilder sb = new StringBuilder();
    File logFile = mainApplication.getCurrentLogFile();
    if (logFile.exists() && logFile.length() > 0) {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    }
    return sb.toString();
  }

  public static String getTimestamp() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date());
  }
}
