package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMIDataLocalHigh {

    public static void main(String[] args) {
        try {
            // Thông tin kết nối từ đề bài
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472";
            String qCode = "MvlngHqi";

            System.out.println("1. Đang kết nối tới RMI Registry tại " + serverIP + ":" + port + "...");
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            DataService dataService = (DataService) registry.lookup("RMIDataService");
            System.out.println("Đã kết nối thành công tới RMIDataService!");

            System.out.println("2. Đang gọi phương thức requestData...");
            Object obj = dataService.requestData(studentCode, qCode);
            

            int[] arr;
            if (obj instanceof int[]) {
                arr = (int[]) obj;
            } else if (obj instanceof Integer[]) {
                Integer[] temp = (Integer[]) obj;
                arr = new int[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    arr[i] = temp[i];
                }
            } else {
                throw new IllegalArgumentException("Dữ liệu nhận về không phải là mảng số nguyên!");
            }

            System.out.print("Mảng dữ liệu nhận được: [");
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + (i < arr.length - 1 ? ", " : ""));
            }
            System.out.println("]");

            List<Integer> peakPositions = new ArrayList<>();
            int n = arr.length;

            if (n == 1) {
                peakPositions.add(1);
            } else if (n > 1) {

                if (arr[0] > arr[1]) {
                    peakPositions.add(1);
                }

                for (int i = 1; i < n - 1; i++) {
                    if (arr[i] > arr[i - 1] && arr[i] > arr[i + 1]) {
                        peakPositions.add(i + 1);
                    }
                }

                if (arr[n - 1] > arr[n - 2]) {
                    peakPositions.add(n);
                }
            }

            System.out.println("Các vị trí đỉnh cục bộ tìm được (bắt đầu từ 1): " + peakPositions);

            System.out.println("3. Đang gửi kết quả qua submitData...");
            dataService.submitData(studentCode, qCode, peakPositions);
            
            System.out.println("4. Gửi dữ liệu thành công! Hoàn thành chương trình.");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình chạy RMI Client:");
            e.printStackTrace();
        }
    }
}

/*
Một chương trình RMI Server cung cấp giao diện DataService trong package RMI, được đăng ký với RegistryServer bằng tên RMIDataService.

Giao diện từ xa:

public interface DataService extends Remote {                               
        public Object requestData(String studentCode, String qCode) throws RemoteException;              
        public void submitData(String studentCode, String qCode, Object data) throws RemoteException;    
    }
Yêu cầu
a. Sử dụng mã sinh viên và mã câu hỏi làm tham số khi gọi RMI. Ví dụ: studentCode="B21DCCN001", qCode="A1B2C3D4".

b. Triệu gọi requestData(studentCode, qCode) để nhận một mảng số nguyên từ server. Ví dụ: [3,8,5,10,7].

c. Một phần tử là đỉnh cục bộ nếu lớn hơn phần tử liền kề bên trái và bên phải; phần tử đầu/cuối chỉ cần lớn hơn phần tử kề duy nhất. Trả về vị trí theo chỉ số bắt đầu từ 1.

d. Triệu gọi submitData(studentCode, qCode, data) để gửi List<Integer> các vị trí đỉnh cục bộ. Ví dụ: [2,4].

e. Kết thúc chương trình client.
*/