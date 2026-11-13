package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Locale;

public class RMIDataStandDevi {

    public static void main(String[] args) {
        try {
            // Thông tin kết nối từ đề bài
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472"; // Mã sinh viên của bạn
            String qCode = "gJBxSdVm";

            System.out.println("1. Đang kết nối tới RMI Registry tại " + serverIP + ":" + port + "...");
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            // Tìm kiếm đối tượng từ xa RMIDataService
            DataService dataService = (DataService) registry.lookup("RMIDataService");
            System.out.println("Đã kết nối thành công tới RMIDataService!");

            // Bước b: Triệu gọi requestData để nhận dữ liệu từ server[cite: 8]
            System.out.println("2. Đang gọi phương thức requestData...");
            Object obj = dataService.requestData(studentCode, qCode);
            
            if (obj == null) {
                System.err.println("Lỗi: Server trả về dữ liệu null! Hãy kiểm tra lại studentCode hoặc qCode.");
                return;
            }

            // Xử lý linh hoạt các kiểu dữ liệu mà server có thể trả về (String, mảng số,...)
            String csvData = "";
            if (obj instanceof String) {
                csvData = (String) obj;
            } else if (obj instanceof double[]) {
                csvData = Arrays.toString((double[]) obj);
                csvData = csvData.substring(1, csvData.length() - 1); // Bỏ ngoặc vuông
            } else if (obj instanceof String[]) {
                csvData = String.join(",", (String[]) obj);
            } else {
                csvData = obj.toString();
            }
            
            System.out.println("Chuỗi dữ liệu thô nhận được: " + csvData);

            // Tách chuỗi CSV thành mảng các số thực double[]
            String[] parts = csvData.split(",");
            int n = parts.length;
            if (n == 0) {
                System.err.println("Lỗi: Mảng dữ liệu nhận được rỗng!");
                return;
            }

            double[] arr = new double[n];
            for (int i = 0; i < n; i++) {
                arr[i] = Double.parseDouble(parts[i].trim());
            }

            // Bước c: Tính toán average, stddev, p95[cite: 8]
            // 1. Tính giá trị trung bình (average)
            double sum = 0;
            for (double val : arr) {
                sum += val;
            }
            double average = sum / n;

            // 2. Tính độ lệch chuẩn (stddev) - Thử dùng công thức mẫu (n - 1) hoặc quần thể (n)
            // Đa số các bài toán thống kê dùng độ lệch chuẩn mẫu (n > 1 ? n - 1 : 1)
            double varianceSum = 0;
            for (double val : arr) {
                varianceSum += Math.pow(val - average, 2);
            }
            double stddev = Math.sqrt(varianceSum / (n > 1 ? n - 1 : 1)); 

            // 3. Tính phân vị 95 (p95): sắp xếp tăng dần, lấy vị trí ceil(n * 0.95) - 1
            Arrays.sort(arr);
            int p95Index = (int) Math.ceil(n * 0.95) - 1;
            if (p95Index >= n) p95Index = n - 1;
            if (p95Index < 0) p95Index = 0;
            double p95 = arr[p95Index];

            // Ép buộc dùng Locale.US để String.format luôn dùng dấu chấm '.' thay vì dấu phẩy ',' cho phần thập phân
            String resultString = String.format(Locale.US, "average=%.2f;stddev=%.2f;p95=%.2f", average, stddev, p95);
            
            System.out.println("Kết quả tính toán gửi lên: " + resultString);

            // Bước d: Triệu gọi submitData để gửi kết quả lên server[cite: 8]
            System.out.println("3. Đang gửi kết quả qua submitData...");
            dataService.submitData(studentCode, qCode, resultString);
            
            System.out.println("4. Gửi dữ liệu thành công! Hoàn thành chương trình.");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình chạy RMI Client:");
            e.printStackTrace();
        }
    }
}