/*Một dịch vụ web (hỗ trợ SOAP version 1.1) được định nghĩa và mô tả trong tệp DataService?wsdl, được triển khai trên server tại URL http://<Exam_IP>:2221/DataService?wsdl để xử lý các bài toán với dữ liệu nguyên thủy.

Yêu cầu: Viết chương trình tại máy trạm (WS client) để giao tiếp với DataService thực hiện các công việc sau:

a. Triệu gọi phương thức getData với tham số đầu vào là mã sinh viên (studentCode) và mã câu hỏi (qCode) để nhận về một danh sách số nguyên (List<Integer>) từ server.

Ví dụ: 7602,9136,1090,34319,7830,6179,10584,20166,28199,30250,32179,22544,3222,10320,30590,19279

b. Thực hiện tìm số lớn nhất có thể tạo dược từ a,b,c,d...

c. Triệu gọi phương thức submitDataString(String studentCode, String qCode, String data) để gửi kết quả tổng đã tính được trở lại server.

Ví dụ: 91367830760261793431932223217930590302502819922544201661927910901058410320

d. Kết thúc chương trình client. */
import ws.DataService_Service;
import ws.DataService;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception{
        String studentCode = "B23DCVT107";
        String qCode = "9T90WySt";
        DataService port = new DataService_Service().getDataServicePort();
        SoapDataSevice sv = sv.getSoapDataServicePort();
        List<Integer> data = port.getData(studentCode, qCode);
        System.out.println("Received: " + data);
        List<String> s = new ArrayList<>();
        for (Integer v : data) s.add(String.valueOf(v));
        s.sort((x, y) -> (y + x).compareTo(x + y));
        StringBuilder sb = new StringBuilder();
        for (String t : s) sb.append(t);
        String result = sb.toString();
        System.out.println("Result: " + result);
        port.submitDataString(studentCode, qCode, result);
        System.out.println("Submitted.");
        }
    }
