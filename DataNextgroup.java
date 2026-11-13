package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DataNextgroup{
    public static void nextPermutation(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        int n = arr.length;

        int i = n - 2;
        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }

        if (i < 0) {
            reverse(arr, 0, n - 1);
            return;
        }

        int j = n - 1;
        while (j > i && arr[j] <= arr[i]) {
            j--;
        }

        swap(arr, i, j);
        reverse(arr, i + 1, n - 1);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }

    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.getRegistry("203.162.10.109", 1099);
        DataService dataService = (DataService) registry.lookup("RMIDataService");

        String studentCode = "B22DCCN029";
        String qCode = "o51mXgdw";

        String data = dataService.requestData(studentCode, qCode).toString();

        String[] tokens = data.split(",\\s*");
        int[] arr = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            arr[i] = Integer.parseInt(tokens[i].trim());
        }

        nextPermutation(arr);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            result.append(arr[i]);
            if (i < arr.length - 1) {
                result.append(",");
            }
        }
        dataService.submitData(studentCode, qCode, result.toString());
    }
}

//[Mã câu hỏi (qCode): o51mXgdw].  Một chương trình (tạm gọi là RMI Server) cung cấp giao diện cho phép triệu gọi từ xa để xử lý dữ liệu.
//Giao diện từ xa:
//public interface DataService extends Remote {
//    public Object requestData(String studentCode, String qCode) throws RemoteException;
//    public void submitData(String studentCode, String qCode, Object data) throws RemoteException;
//}
//Trong đó:
//        •	Interface DataService được viết trong package RMI.
//•	Đối tượng cài đặt giao diện từ xa DataService được đăng ký với RegistryServer với tên là: RMIDataService.
//Yêu cầu: Viết chương trình tại máy trạm (RMI client) để thực hiện các công việc sau với dữ liệu nhận được từ RMI Server:
//a. Triệu gọi phương thức requestData để nhận một chuỗi các số nguyên.
//b. Sử dụng thuật toán sinh tổ hợp kế tiếp để tìm tổ hợp kế tiếp của chuỗi số này theo thứ tự từ điển. Nếu chuỗi đã là tổ hợp lớn nhất, trả về tổ hợp nhỏ nhất (sắp xếp lại từ đầu theo thứ tự từ điển).
//Ví dụ: Với chuỗi 1, 2, 3 tổ hợp kế tiếp là 1, 3, 2. Nếu chuỗi là 3, 2, 1 (tổ hợp lớn nhất), kết quả sẽ là 1, 2, 3 (tổ hợp nhỏ nhất).
//c. Triệu gọi phương thức submitData để gửi chuỗi chứa tổ hợp kế tiếp đã tìm được trở lại server.
//d. Kết thúc chương trình client.
