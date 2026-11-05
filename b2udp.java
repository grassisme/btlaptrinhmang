import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class laptrinhmang {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("36.50.135.242");
        int port = 2208;
        String studentCode = "B23DCVT107";
        String qCode = "z3M7cRz4"; // nên copy bằng nút copy

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            // a. Gửi ";studentCode;qCode"
            byte[] s = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(s, s.length, host, port));

            // b. Nhận "requestId;data"
            byte[] buf = new byte[4096];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            socket.receive(recv);
            String msg = new String(recv.getData(), 0, recv.getLength(), StandardCharsets.UTF_8).trim();
            System.out.println("Received: " + msg);

            int idx = msg.indexOf(';');
            String requestId = msg.substring(0, idx).trim();
            String data = msg.substring(idx + 1).trim();

            // c. Đếm tần suất
            Map<Character, Integer> cnt = new HashMap<>();
            for (char c : data.toCharArray()) cnt.merge(c, 1, Integer::sum);

            // Chọn ký tự có tần suất max, hòa thì lấy ký tự xuất hiện sớm nhất
            char best = data.charAt(0);
            for (char c : data.toCharArray())
                if (cnt.get(c) > cnt.get(best)) best = c;

            StringBuilder pos = new StringBuilder();
            for (int i = 0; i < data.length(); i++)
                if (data.charAt(i) == best) pos.append(i + 1).append(",");

            String result = requestId + ";" + best + ":" + pos;
            byte[] o = result.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(o, o.length, recv.getAddress(), recv.getPort()));
            System.out.println("Sent: " + result);
        } // d. tự đóng socket
    }
}