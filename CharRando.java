package RMI;

import java.net.URI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMICharRando {

    public static void main(String[] args) {
        try {
            String serverIP = "36.50.135.242";
            int port = 1099; 
            String studentCode = "B23DCCN472"; 
            String qCode = "3JErMnFA";

            System.out.println("1. Đang kết nối tới RMI Registry tại " + serverIP + ":" + port + "...");
            Registry registry = LocateRegistry.getRegistry(serverIP, port);

            CharacterService charService = (CharacterService) registry.lookup("RMICharacterService");
            System.out.println("Đã kết nối thành công tới RMICharacterService!");

            System.out.println("2. Đang gọi phương thức requestCharacter...");
            String inputStr = charService.requestCharacter(studentCode, qCode);
            System.out.println("Chuỗi nhận được từ server: " + inputStr);

            URI uri = new URI(null, null, inputStr, null);
            String encodedStr = uri.toASCIIString();
            
            System.out.println("Chuỗi sau khi mã hóa URL: " + encodedStr);

            System.out.println("3. Đang gửi kết quả qua submitCharacter...");
            charService.submitCharacter(studentCode, qCode, encodedStr);
            
            System.out.println("4. Gửi dữ liệu thành công! Hoàn thành chương trình.");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình chạy RMI Client:");
            e.printStackTrace();
        }
    }
}

//Một chương trình (tạm gọi là RMI Server) cung cấp giao diện cho phép triệu gọi từ xa để xử lý chuỗi.

//Giao diện từ xa:

//public interface CharacterService extends Remote {
//    public String requestCharacter(String studentCode, String qCode) throws RemoteException;
 //   public void submitCharacter(String studentCode, String qCode, String strSubmit) throws RemoteException;

//Trong đó:

//Interface CharacterService được viết trong package RMI.
//Đối tượng cài đặt giao diện từ xa CharacterService được đăng ký với RegistryServer với tên là: RMICharacterService.
//Yêu cầu: Viết chương trình tại máy trạm (RMI client) để thực hiện các công việc sau với chuỗi được nhận từ RMI Server:

//a. Triệu gọi phương thức requestCharacter để nhận chuỗi ngẫu nhiên từ server. Chuỗi này gồm chữ, số, dấu cách và các ký tự đặc biệt như ~=+%^*/()[]{}!@#$?|.

//b. Thực hiện thao tác mã hóa URL (URL Encoding) cho chuỗi đầu vào nhận được từ server. Mã hóa theo quy tắc phần đường dẫn của URI (tương đương new java.net.URI(null, null, s, null).toASCIIString()): dấu cách, ký tự không hợp lệ trong URI (như % " < > [ ] { } | \ ^), ?, # và ký tự ngoài ASCII được thay bằng %XX (UTF-8); chữ, số và - _ . ~ ! * ' ( ) ; : @ & = + $ , / giữ nguyên.

//Ví dụ: Chuỗi ban đầu "Hello World!" -> Chuỗi mã hóa URL là: "Hello%20World!"

//c. Triệu gọi phương thức submitCharacter để gửi chuỗi đã được mã hóa trở lại server.

//d. Kết thúc chương trình client.