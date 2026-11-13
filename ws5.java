/*cd "G:\hoc code\ltm\src"
& "C:\Program Files\Java\jdk1.8.0_XXX\bin\wsimport.exe" -keep -s . -p wsobj "http://36.50.135.242:2221/ObjectService?wsdl"

Một dịch vụ web (hỗ trợ SOAP version 1.1) được định nghĩa và mô tả trong tệp ObjectService.wsdl, được triển khai trên server tại URL http://<Exam_IP>:2221/ObjectService?wsdl để xử lý các bài toán với đối tượng.

Yêu cầu: Viết chương trình tại máy trạm (WS client) để giao tiếp với ObjectService thực hiện các công việc sau:

a. Triệu gọi phương thức requestListEmployeeY với tham số đầu vào là mã sinh viên (studentCode) và mã câu hỏi (qCode) để nhận về một danh sách đối tượng EmployeeY từ server. Mỗi đối tượng EmployeeY có các thuộc tính:

name: kiểu String, đại diện cho tên của nhân viên.
startDate: kiểu Date, đại diện cho ngày bắt đầu làm việc của nhân viên.
b. Sắp xếp danh sách EmployeeY theo thứ tự thâm niên từ cao đến thấp (người có ngày bắt đầu làm việc sớm nhất sẽ đứng đầu danh sách).

Nếu hai nhân viên có cùng ngày bắt đầu làm việc, giữ nguyên thứ tự ban đầu của họ trong danh sách.

c. Triệu gọi phương thức submitListEmployeeY(String studentCode, String qCode, List<EmployeeY> data) để gửi danh sách nhân viên đã sắp xếp trở lại server.

d. Kết thúc chương trình client.
*/
import wsobj.ObjectService_Service;
import wsobj.ObjectService;
import wsobj.EmployeeY;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String studentCode = "B23DCVT107";
        String qCode = "JWFPmNyX";

        ObjectService port = new ObjectService_Service().getObjectServicePort();

        // a
        List<EmployeeY> list = port.requestListEmployeeY(studentCode, qCode);
        System.out.println("Received " + list.size() + " employees:");
        for (EmployeeY e : list) {
            System.out.println("  " + e.getName() + " - " + e.getStartDate());
        }

        // b: startDate tang dan, stable sort giu nguyen thu tu khi trung
        list.sort(Comparator.comparing(EmployeeY::getStartDate));

        System.out.println("Sorted:");
        for (EmployeeY e : list) {
            System.out.println("  " + e.getName() + " - " + e.getStartDate());
        }

        // c
        port.submitListEmployeeY(studentCode, qCode, list);
        System.out.println("Submitted.");
    }
}