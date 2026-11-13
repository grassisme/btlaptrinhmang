package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMIBytemax {

    public static void main(String[] args) {
        try {
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472";
            String qCode = "npAPT3Jg";
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            ByteService byteService = (ByteService) registry.lookup("RMIByteService");

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

            byte maxFreqElement = data[0];
            int maxFreq = -1;

            for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > maxFreq) {
                    maxFreq = entry.getValue();
                    maxFreqElement = entry.getKey();
                }
            }

            System.out.println("Phần tử xuất hiện nhiều nhất: " + maxFreqElement + " (Số lần: " + maxFreq + ")");


            byte[] resultData = new byte[] { maxFreqElement, (byte) maxFreq };

  
            byteService.submitData(studentCode, qCode, resultData);
            

        } catch (Exception e) {

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

b. Tìm phần tử xuất hiện nhiều nhất trong mảng byte[]. Nếu có nhiều phần tử có cùng số lần xuất hiện cao nhất, chỉ cần trả về phần tử đầu tiên xuất hiện trong các phần tử đó.

Ví dụ: Nếu mảng dữ liệu nhận được là [1, 2, 3, 2, 1, 2], phần tử xuất hiện nhiều nhất là 2, với tần suất xuất hiện 3 lần, nên mảng gửi lên server là [2, 3].

c. Triệu gọi phương thức submitData để gửi mảng byte chứa phần tử phổ biến nhất, cùng với tần suất xuất hiện của nó trở lại server.

d. Kết thúc chương trình client
 */