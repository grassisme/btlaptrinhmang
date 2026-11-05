import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class b1udp {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("36.50.135.242");
        int port = 2207;
        String studentCode = "B23DCVT107";
        String qCode = "lJIqex47";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(10000);

            // a. Gửi ";studentCode;qCode"
            byte[] sendData = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(sendData, sendData.length, host, port));

            // b. Nhận "requestId;n;A1,A2,...,Am"
            byte[] buf = new byte[4096];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            socket.receive(recv);
            String data = new String(recv.getData(), 0, recv.getLength(), StandardCharsets.UTF_8).trim();
            System.out.println("Received: " + data);

            String[] parts = data.split(";", 3);
            String requestId = parts[0].trim();
            int n = Integer.parseInt(parts[1].trim());

            // c. Tìm giá trị còn thiếu trong 1..n
            boolean[] seen = new boolean[n + 1];
            if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                for (String s : parts[2].split(",")) {
                    int v = Integer.parseInt(s.trim());
                    if (v >= 1 && v <= n) seen[v] = true;
                }
            }
            StringJoiner missing = new StringJoiner(",");
            for (int i = 1; i <= n; i++) if (!seen[i]) missing.add(String.valueOf(i));

            String result = requestId + ";" + missing;
            byte[] out = result.getBytes(StandardCharsets.UTF_8);
            // Gửi về đúng địa chỉ/cổng mà server đã trả lời
            socket.send(new DatagramPacket(out, out.length, recv.getAddress(), recv.getPort()));
            System.out.println("Sent: " + result);
        } // d. try-with-resources tự đóng socket
    }
}