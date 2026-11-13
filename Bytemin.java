package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMIBytemin {

    public static void main(String[] args) {
        try {
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472"; 
            String qCode = "xSebCD7V";

            System.out.println("1. Đang kết nối tới RMI Registry tại " + serverIP + ":" + port + "...");
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            ByteService byteService = (ByteService) registry.lookup("RMIByteService");
            System.out.println("Đã kết nối thành công tới RMIByteService!");

            System.out.println("2. Đang gọi phương thức requestData...");
            byte[] data = byteService.requestData(studentCode, qCode);
            
            System.out.print("Mảng dữ liệu nhận được: [");
            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i] + (i < data.length - 1 ? ", " : ""));
            }
            System.out.println("]");

            Map<Byte, Integer> frequencyMap = new LinkedHashMap<>();
            for (byte b : data) {
                frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
            }

            byte minFreqElement = data[0];
            int minFreq = Integer.MAX_VALUE;


            for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() < minFreq) {
                    minFreq = entry.getValue();
                    minFreqElement = entry.getKey();
                }
            }

            System.out.println("Phần tử xuất hiện ít nhất: " + minFreqElement + " (Số lần: " + minFreq + ")");

            byte[] resultData = new byte[] { minFreqElement, (byte) minFreq };

            System.out.println("3. Đang gửi kết quả qua submitData...");
            byteService.submitData(studentCode, qCode, resultData);
            
            System.out.println("4. Gửi dữ liệu thành công! Hoàn thành chương trình.");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình chạy RMI Client:");
            e.printStackTrace();
        }
    }
}

/*
Một chương trình (tạm gọi là RMI Server) cung cấp giao diện cho phép triệu gọi từ xa để xử lý dữ liệu nhị phân.

Giao diện từ xa:

public interface ByteService extends Remote {
    public byte[] requestData(String studentCode, String qCode) throws RemoteException;
    public void submitData(String studentCode, String qCode, byte[] data) throws RemoteException;
}
Trong đó:

Interface ByteService được viết trong package RMI.
Đối tượng cài đặt giao diện từ xa ByteService được đăng ký với RegistryServer với tên là: RMIByteService.

Yêu cầu: Viết chương trình tại máy trạm (RMI client) để thực hiện các công việc sau với dữ liệu nhị phân nhận được từ RMI Server:

a. Triệu gọi phương thức requestData để nhận một mảng dữ liệu nhị phân (byte[]) từ server.

b. Tìm phần tử có số lần xuất hiện ít nhất trong mảng, nếu có nhiều phần tử có cùng số lần xuất hiện ít nhất, chỉ cần trả về phần tử đầu tiên xuất hiện trong các phần tử đó

Ví dụ: Nếu mảng dữ liệu nhận được là [1, 2, 3, 2, 1], chương trình sẽ tìm ra phần tử xuất hiện ít nhất là 3 (xuất hiện 1 lần), nên mảng gửi lên server là [3, 1].

c. Triệu gọi phương thức submitData để gửi mảng byte kết quả chứa phẩn tử có số lần xuất hiện ít nhất và số lần xuất hiện trở lại server.

d. Kết thúc chương trình client.
*/