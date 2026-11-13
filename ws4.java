/*Một dịch vụ web (hỗ trợ SOAP version 1.1) được định nghĩa và mô tả trong tệp CharacterService.wsdl, được triển khai trên server tại URL http://<Exam_IP>:2221/CharacterService?wsdl để xử lý các bài toán về chuỗi và ký tự.

Yêu cầu: Viết chương trình tại máy trạm (WS client) để giao tiếp với CharacterService thực hiện các công việc sau:

a. Triệu gọi phương thức requestStringArray với tham số đầu vào là mã sinh viên (studentCode) và mã câu hỏi (qCode) để nhận về một danh sách chuỗi (List<String>) từ server.

b. Phân loại các từ trong mảng chuỗi thành các nhóm có cùng số lượng nguyên âm. Tạo một chuỗi cho mỗi nhóm, trong đó liệt kê các từ cách nhau bằng dấu phẩy, và sắp xếp các từ theo thứ tự từ điển trong mỗi nhóm.

c. Triệu gọi phương thức submitCharacterStringArray(String studentCode, String qCode, List<String> data) để gửi danh sách chuỗi kết quả trở lại server, trong đó mỗi phần tử là một nhóm từ với cùng số lượng nguyên âm.

Ví dụ: Nếu danh sách chuỗi nhận được từ phương thức requestCharacter là [apple, banana, pear, grape, kiwi], các nhóm có thể là:

Nhóm 2 nguyên âm: apple, banana
Nhóm 1 nguyên âm: grape, kiwi, pear
Danh sách kết quả sẽ là [apple, banana, grape, kiwi, pear], và danh sách này sẽ được gửi lại server qua phương thức submitCharacter.

d. Kết thúc chương trình client.*/
import wschar.CharacterService_Service;
import wschar.CharacterService;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String studentCode = "B23DCVT107";
        String qCode = "...";   // dien ma cau hoi

        CharacterService port = new CharacterService_Service().getCharacterServicePort();

        // a
        List<String> words = port.requestStringArray(studentCode, qCode);
        System.out.println("Received: " + words);

        // b
        Map<Integer, List<String>> groups = new TreeMap<>(Collections.reverseOrder());
        for (String w : words) {
            groups.computeIfAbsent(countVowels(w), x -> new ArrayList<>()).add(w);
        }

        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> e : groups.entrySet()) {
            List<String> g = e.getValue();
            Collections.sort(g);
            result.add(String.join(", ", g));
        }
        System.out.println("Result: " + result);

        // c
        port.submitCharacterStringArray(studentCode, qCode, result);
        System.out.println("Submitted.");
    }

    static int countVowels(String w) {
        int c = 0;
        for (char ch : w.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(ch) >= 0) c++;
        }
        return c;
    }
}