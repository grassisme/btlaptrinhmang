tạo thư mục UDP
file 1: 
package UDP;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 20171107L;

    private String id, code, name, email;

    public Student(String id, String code, String name, String email) {
        this.id = id; this.code = code; this.name = name; this.email = email;
    }
    public Student(String code) { this.code = code; }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return id + " | " + code + " | " + name + " | " + email;
    }
}

ra ngoài thư mục UDP
file 2:
import UDP.Student;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class laptrinhmang {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("36.50.135.242");
        int port = 2209;
        String studentCode = "B23DCVT107";
        String qCode = "SaX3TZO1"; // copy bằng nút copy (O hay 0?)

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            // a. Gửi ";studentCode;qCode"
            byte[] s = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(s, s.length, host, port));

            // b. Nhận: 8 byte requestId + object
            byte[] buf = new byte[65535];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            socket.receive(recv);
            int len = recv.getLength();

            byte[] reqId = Arrays.copyOfRange(buf, 0, 8);
            System.out.println("requestId: " + new String(reqId, StandardCharsets.UTF_8));

            Student st;
            try (ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(buf, 8, len - 8))) {
                st = (Student) ois.readObject();
            }
            System.out.println("Received: " + st);

            // c. Chuẩn hóa tên + tạo email
            String[] words = st.getName().trim().toLowerCase().split("\\s+");
            StringBuilder name = new StringBuilder();
            for (String w : words)
                name.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            st.setName(name.toString().trim());

            StringBuilder email = new StringBuilder(words[words.length - 1]);
            for (int i = 0; i < words.length - 1; i++) email.append(words[i].charAt(0));
            st.setEmail(email + "@ptit.edu.vn");
            System.out.println("Modified: " + st);

            // Serialize object -> byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(st);
            }
            byte[] obj = bos.toByteArray();

            // Ghép 8 byte requestId + object
            byte[] out = new byte[8 + obj.length];
            System.arraycopy(reqId, 0, out, 0, 8);
            System.arraycopy(obj, 0, out, 8, obj.length);
            socket.send(new DatagramPacket(out, out.length, recv.getAddress(), recv.getPort()));
            System.out.println("Sent.");
        } // d. tự đóng socket
    }
}