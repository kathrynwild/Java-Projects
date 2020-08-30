import java.util.Scanner;

public class AES_Test {

    private static String[] key;
    private static String[] ciphertext;
    private static String[] plaintext;

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter 1 to view the encryption and decryption of the plaintext given in Appendix C of the " +
                "FIPS 197 document, enter 2 to see the decryption of the ciphertext given in the project description, or " +
                "enter 3 to make your own key and plaintext array pair: ");
        int choice = in.nextInt();

        if(choice == 1){
            Deliverables1();
        }
        else if(choice == 2){
            Deliverables2();
        }
        else{
            DIY();
        }
    }

    public static void Deliverables1(){
        key = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c", "0d", "0e", "0f"};
        plaintext = new String[] {"00", "11", "22", "33", "44", "55", "66", "77", "88", "99", "aa", "bb", "cc", "dd", "ee", "ff"};
        ciphertext = new String[]{"69", "c4", "e0", "d8", "6a" ,"7b", "04", "30", "d8", "cd", "b7", "80", "70", "b4", "c5", "5a"};

        Encryption encrypt = new Encryption(key, plaintext);
        Decryption decrypt = new Decryption(key, ciphertext);
    }

    public static void Deliverables2(){
        key = new String[]{"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F"};
        ciphertext = new String[] {"F4", "35", "15", "03", "AA", "78", "1C", "52", "02", "67", "D6", "90", "C4", "2D", "1F", "43"};

        Decryption decrypt = new Decryption(key, ciphertext);
    }

    public static void DIY() {
        int keyLength, textLength;

        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the length of the key (in 2 digit increments): ");
        keyLength = in.nextInt();
        key = new String[keyLength];

        System.out.println("Please enter the length of the plaintext (in 2 digit increments): ");
        textLength = in.nextInt();
        plaintext = new String[textLength];

        System.out.println("Please enter the key array elements in 2 digit increments (ex. 5F). First element: ");
        for(int i=0; i<key.length; i++){
            key[i] = in.nextLine();
        }

        System.out.println("Please enter the plaintext array elements in 2 digit increments (ex. 5F). First element: ");
        for(int i=0; i<plaintext.length; i++){
            plaintext[i] = in.nextLine();
        }

        Encryption encrypt = new Encryption(key, plaintext);
        Decryption decrypt = new Decryption(key, ciphertext);
    }
}
