/*Một dịch vụ web (hỗ trợ SOAP version 1.1) được định nghĩa và mô tả trong tệp ObjectService.wsdl, được triển khai trên server tại URL http://<Exam_IP>:2221/ObjectService?wsdl để xử lý các bài toán với đối tượng.

Yêu cầu: Viết chương trình tại máy trạm (WS client) để giao tiếp với ObjectService thực hiện các công việc sau:

a. Triệu gọi phương thức requestListCustomer với tham số đầu vào là mã sinh viên (studentCode) và mã câu hỏi (qCode) để nhận về danh sách đối tượng Customer từ server. Mỗi đối tượng Customer có các thuộc tính:

customerId: kiểu String, đại diện cho mã khách hàng.
location: kiểu String, đại diện cho địa điểm của khách hàng.
purchaseCount: kiểu int, đại diện cho số lần mua hàng của khách hàng.
totalSpent: kiểu float, đại diện cho tổng số tiền đã chi tiêu của khách hàng.
b. Lọc và giữ lại các khách hàng có totalSpent lớn hơn 5000 và có purchaseCount từ 5 trở lên.

c. Triệu gọi phương thức submitListCustomer(String studentCode, String qCode, List<Customer> customers) để gửi danh sách các khách hàng tiềm năng trở lại server.

d. Kết thúc chương trình client.
*/
import wsobj.ObjectService_Service;
import wsobj.ObjectService;
import wsobj.Customer;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String studentCode = "B23DCVT107";
        String qCode = "ciCso506";

        ObjectService port = new ObjectService_Service().getObjectServicePort();

        // a
        List<Customer> list = port.requestListCustomer(studentCode, qCode);
        System.out.println("Received " + list.size() + " customers");

        // b: totalSpent > 5000 VA purchaseCount >= 5
        List<Customer> result = new ArrayList<>();
        for (Customer c : list) {
            if (c.getTotalSpent() > 5000 && c.getPurchaseCount() >= 5) {
                result.add(c);
            }
        }

        System.out.println("Filtered " + result.size() + ":");
        for (Customer c : result) {
            System.out.println("  " + c.getCustomerId() + " | " + c.getLocation()
                    + " | " + c.getPurchaseCount() + " | " + c.getTotalSpent());
        }

        // c
        port.submitListCustomer(studentCode, qCode, result);
        System.out.println("Submitted.");
    }
}