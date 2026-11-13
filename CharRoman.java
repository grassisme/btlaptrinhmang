package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMICharRoman {

    public static void main(String[] args) {
        try {
            // Thông tin kết nối từ đề bài
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472"; // <-- THAY MÃ SINH VIÊN CỦA BẠN VÀO ĐÂY
            String qCode = "WABuLu2Z";

            System.out.println("1. Đang kết nối tới RMI Registry tại " + serverIP + ":" + port + "...");
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            // Tìm kiếm đối tượng từ xa RMICharacterService[cite: 6]
            CharacterService charService = (CharacterService) registry.lookup("RMICharacterService");
            System.out.println("Đã kết nối thành công tới RMICharacterService!");

            // Bước a: Triệu gọi phương thức requestCharacter để nhận chuỗi số thập phân[cite: 6]
            System.out.println("2. Đang gọi phương thức requestCharacter...");
            String inputStr = charService.requestCharacter(studentCode, qCode);
            System.out.println("Chuỗi số thập phân nhận được: " + inputStr);

            // Bước b: Chuyển đổi số thập phân sang số La Mã[cite: 6]
            int number = Integer.parseInt(inputStr.trim());
            String romanNumeral = convertToRoman(number);
            System.out.println("Số La Mã sau khi chuyển đổi: " + romanNumeral);

            // Bước c: Triệu gọi phương thức submitCharacter để gửi kết quả trở lại server[cite: 6]
            System.out.println("3. Đang gửi kết quả qua submitCharacter...");
            charService.submitCharacter(studentCode, qCode, romanNumeral);
            
            System.out.println("4. Gửi dữ liệu thành công! Hoàn thành chương trình.");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình chạy RMI Client:");
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ chuyển đổi số nguyên sang chuỗi số La Mã
    private static String convertToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(symbols[i]);
            }
        }
        return roman.toString();
    }
}

/*Một chương trình (tạm gọi là RMI Server) cung cấp giao diện cho phép triệu gọi từ xa để xử lý chuỗi.

Giao diện từ xa:

public interface CharacterService extends Remote {
    public String requestCharacter(String studentCode, String qCode) throws RemoteException;
    public void submitCharacter(String studentCode, String qCode, String strSubmit) throws RemoteException;
}
Trong đó:

Interface CharacterService được viết trong package RMI.
Đối tượng cài đặt giao diện từ xa CharacterService được đăng ký với RegistryServer với tên là: RMICharacterService.
Yêu cầu: Viết chương trình tại máy trạm (RMI client) để thực hiện các công việc sau với chuỗi được nhận từ RMI Server:

a. Triệu gọi phương thức requestCharacter để nhận chuỗi ngẫu nhiên từ server với định dạng: "Số thập phân đầu vào".

b. Thực hiện chuyển đổi số thập phân nhận được thành chuỗi số La Mã (Roman).

Quy tắc chuyển đổi:

Các ký tự La Mã chính bao gồm: I=1, V=5, X=10, L=50, C=100, D=500, M=1000.

Ví dụ: 58 -> "LVIII"

c. Triệu gọi phương thức submitCharacter để gửi chuỗi số La Mã đã chuyển đổi trở lại server.

d. Kết thúc chương trình client.
*/