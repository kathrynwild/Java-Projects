import java.util.Arrays;

public class Encryption {
    private String[] key;           // given key array
    private String[] plaintext;     // given plaintext array
    private String[] text;          // array to print each step to user
    private String[] tempText;      // array to hold values to xor with key schedule
    private int round;              // holds global round value

    private int Nk=4;               // AES 128 - Nk=4
    private int Nb=4;               // AES 128 - Nb=4
    private int Nr=10;              // AES 128 - Nk=10
    int[] w = new int[Nb*(Nr+1)];   // subkey words

    Encryption(String[] k, String[] p) {
        key = k;
        plaintext = p;
        text = new String[plaintext.length];
        tempText = new String[key.length];

        // use loop to make text array identical to plaintext array and avoid pointers
        for(int i=0; i<plaintext.length; i++){
            text[i] = plaintext[i];
            tempText[i] = key[i];
        }

        System.out.println("PLAINTEXT: " + Arrays.toString(plaintext));
        System.out.println("KEY: " + Arrays.toString(key));
        keyExpansion();
        System.out.println();
        System.out.println("CIPHER (ENCRYPT): ");

        for (round = 1; round < 11; round++){
            System.out.println();
            System.out.println("Round " + round);

            Start();
            byteSubstitution();
            shiftRows();
            mixColumns();
            keySchedule();
        }

        System.out.println();

        System.out.println("CIPHERTEXT: ");
        Start();
        System.out.println();
    }

    // this method follows Figure 11 from NIST.FIPS.197.pdf
    private void keyExpansion(){
        int temp;
        int i = 0;
        int[] decKey = hex2Decimal(key);

        while (i < Nk) {
            w[i] = toWord(decKey[4 * i], decKey[4 * i + 1], decKey[4 * i + 2], decKey[4 * i + 3]);
            i++;
        }

        i = Nk;

        while (i < Nb*(Nr+1)) {
            temp = w[i - 1];
            if (i % Nk == 0) {
                temp = subWord(rotWord(temp)) ^ Rcon[i / 4];
            }
            else if (Nk > 6 && i % Nk == 4) {
                temp = subWord(temp);
            }
            w[i] = w[i - Nk] ^ temp;
            i++;
        }

        System.out.println("KEY EXPANSION SUBKEY: " + Arrays.toString(decimal2Hex(w)));
    }

    // this method xors previous 2 hex arrays displayed to the screen
    private void Start(){
        int[] finalText = new int[text.length];
        int[] decText = hex2Decimal(text);          // convert hex array to decimal to xor with temp
        int[] decKey = hex2Decimal(tempText);       // convert hex array to decimal to xor with text

        // xor each number in arrays
        for(int i=0; i<text.length; i++){
            finalText[i] = decText[i] ^ decKey[i];
        }

        // convert arrya back to hex
        text = decimal2Hex(finalText);

        //fix single digits to add '0' to front
        for(int i=0; i<text.length; i++){
            if(text[i].length() == 1){
                text[i] = '0' + text[i];
            }
            // convert to uppercase
            text[i] = text[i].toUpperCase();
        }

        System.out.println("START: " + Arrays.toString(text));
    }

    // this method uses table 4.3 from the book to do byte substitution
    private void byteSubstitution() {
        // array to find placement of each value in the text array
        String[] xy = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
                        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
                        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
                        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
                        "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
                        "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
                        "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
                        "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
                        "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
                        "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
                        "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
                        "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
                        "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
                        "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
                        "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
                        "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"  };

        // array from table 4.3 matrix
        String[] sub = {"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76",
                        "CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0",
                        "B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15",
                        "04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75",
                        "09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84",
                        "53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF",
                        "D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8",
                        "51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2",
                        "CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73",
                        "60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB",
                        "E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79",
                        "E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08",
                        "BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A",
                        "70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E",
                        "E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF",
                        "8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"  };

        // temp array to avoid pointers
        String[] temp = new String[text.length];
        for(int i=0; i<text.length; i++){
            temp[i] = text[i];
        }

        //using placement from xy array to match up to placement in sub array
        for (int i = 0; i < plaintext.length; i++) {
            for (int j = 0; j < xy.length; j++) {
                if (xy[j].equals(text[i])) {
                    temp[i] = sub[j];
                }
            }
        }

        text = temp;

        System.out.println("BYTE SUBSTITUTION: " + Arrays.toString(text));
    }

    // this method moves numbers in array to new spots to shift the rows
    private void shiftRows() {
        //array with new spots to be moved to
        int[] newSpots = {  0, 5, 10, 15,
                            4, 9, 14, 3,
                            8, 13, 2, 7,
                            12, 1, 6, 11    };

        // new array to avoid pointers
        String[] temp = new String[text.length];

        for (int i = 0; i < text.length; i++) {
            temp[i] = text[newSpots[i]];
        }

        text = temp;

        System.out.println("SHIFT ROWS: " + Arrays.toString(text));
    }

    // this method mixes each column of the state matrix
    private void mixColumns() {
        // constants is the matrix from book (pg 105) to get the C matrix
        int[] constants = { 2, 3, 1, 1,
                            1, 2, 3, 1,
                            1, 1, 2, 3,
                            3, 1, 1, 2  };

        int[] decimalStr = hex2Decimal(text);       // convert hex array to decimal array
        int[] temp = new int[decimalStr.length];    //create temp array to avoid pointers

        // (c0*b0) xor (c1*b4) xor (c2*b8) xor (c3*b12)
        // each row of decimalStr gets GF(2^8) with constant matrix
        temp[0] = Galois.multiply(constants[0], decimalStr[0]) ^ Galois.multiply(constants[1], decimalStr[1])
                ^ Galois.multiply(constants[2], decimalStr[2]) ^ Galois.multiply(constants[3], decimalStr[3]);
        temp[1] = Galois.multiply(constants[4], decimalStr[0]) ^ Galois.multiply(constants[5], decimalStr[1])
                ^ Galois.multiply(constants[6], decimalStr[2]) ^ Galois.multiply(constants[7], decimalStr[3]);
        temp[2] = Galois.multiply(constants[8], decimalStr[0]) ^ Galois.multiply(constants[9], decimalStr[1])
                ^ Galois.multiply(constants[10], decimalStr[2]) ^ Galois.multiply(constants[11], decimalStr[3]);
        temp[3] = Galois.multiply(constants[12], decimalStr[0]) ^ Galois.multiply(constants[13], decimalStr[1])
                ^ Galois.multiply(constants[14], decimalStr[2]) ^ Galois.multiply(constants[15], decimalStr[3]);

        temp[4] = Galois.multiply(constants[0], decimalStr[4]) ^ Galois.multiply(constants[1], decimalStr[5])
                ^ Galois.multiply(constants[2], decimalStr[6]) ^ Galois.multiply(constants[3], decimalStr[7]);
        temp[5] = Galois.multiply(constants[4], decimalStr[4]) ^ Galois.multiply(constants[5], decimalStr[5])
                ^ Galois.multiply(constants[6], decimalStr[6]) ^ Galois.multiply(constants[7], decimalStr[7]);
        temp[6] = Galois.multiply(constants[8], decimalStr[4]) ^ Galois.multiply(constants[9], decimalStr[5])
                ^ Galois.multiply(constants[10], decimalStr[6]) ^ Galois.multiply(constants[11], decimalStr[7]);
        temp[7] = Galois.multiply(constants[12], decimalStr[4]) ^ Galois.multiply(constants[13], decimalStr[5])
                ^ Galois.multiply(constants[14], decimalStr[6]) ^ Galois.multiply(constants[15], decimalStr[7]);

        temp[8] = Galois.multiply(constants[0], decimalStr[8]) ^ Galois.multiply(constants[1], decimalStr[9])
                ^ Galois.multiply(constants[2], decimalStr[10]) ^ Galois.multiply(constants[3], decimalStr[11]);
        temp[9] = Galois.multiply(constants[4], decimalStr[8]) ^ Galois.multiply(constants[5], decimalStr[9])
                ^ Galois.multiply(constants[6], decimalStr[10]) ^ Galois.multiply(constants[7], decimalStr[11]);
        temp[10] = Galois.multiply(constants[8], decimalStr[8]) ^ Galois.multiply(constants[9], decimalStr[9])
                ^ Galois.multiply(constants[10], decimalStr[10]) ^ Galois.multiply(constants[11], decimalStr[11]);
        temp[11] = Galois.multiply(constants[12], decimalStr[8]) ^ Galois.multiply(constants[13], decimalStr[9])
                ^ Galois.multiply(constants[14], decimalStr[10]) ^ Galois.multiply(constants[15], decimalStr[11]);

        temp[12] = Galois.multiply(constants[0], decimalStr[12]) ^ Galois.multiply(constants[1], decimalStr[13])
                ^ Galois.multiply(constants[2], decimalStr[14]) ^ Galois.multiply(constants[3], decimalStr[15]);
        temp[13] = Galois.multiply(constants[4], decimalStr[12]) ^ Galois.multiply(constants[5], decimalStr[13])
                ^ Galois.multiply(constants[6], decimalStr[14]) ^ Galois.multiply(constants[7], decimalStr[15]);
        temp[14] = Galois.multiply(constants[8], decimalStr[12]) ^ Galois.multiply(constants[9], decimalStr[13])
                ^ Galois.multiply(constants[10], decimalStr[14]) ^ Galois.multiply(constants[11], decimalStr[15]);
        temp[15] = Galois.multiply(constants[12], decimalStr[12]) ^ Galois.multiply(constants[13], decimalStr[13])
                ^ Galois.multiply(constants[14], decimalStr[14]) ^ Galois.multiply(constants[15], decimalStr[15]);

        // convert int array to hex array
        for(int i=0; i< temp.length; i++){
            text[i]= (decimal2Hex(temp)[i]);
            tempText[i] = (decimal2Hex(temp)[i]);
        }
        //System.out.println(Arrays.toString(temp));
        System.out.println("MIX COLUMNS: " + Arrays.toString(text));
    }

    // this method prints out the derived subkeys to use from the key expansion
    private void keySchedule(){
        //String[] wTemp = {"D6", "AA", "74", "FD", "D2", "AF", "72", "FA", "DA", "A6", "78", "F1", "D6", "AB", "76", "FE"};
        int[] wTemp = new int[tempText.length];

        int j=0;
        for(int i=round*Nb; i<=(round+1)*Nb-1; i++){
            wTemp[j] = w[i];
            wTemp[j+4] = w[i];
            wTemp[j+8] = w[i];
            wTemp[j+12] = w[i];
            j++;
        }

        String[] hexWTemp = decimal2Hex(wTemp);
        for(int i=0; i<text.length; i++){
            tempText[i] = hexWTemp[i];
        }

        System.out.println("KEY SCHEDULE: " + Arrays.toString(tempText));
    }

    // this method converts a hexadecimal string array to a decimal array
    private int[] hex2Decimal(String[] t) {
        int[] decimalStr = new int[t.length];

        for(int i=0; i<t.length; i++) {
            //hex to binary conversion
            decimalStr[i] = Integer.parseInt(t[i], 16);
        }

        return decimalStr;
    }

    // this method converts a decimal array to a hexadecimal string array
    private String[] decimal2Hex(int[] t){
        String[] hexStr = new String[t.length];

        for(int i=0; i<t.length; i++) {
            //decimal to hex conversion
            hexStr[i] = Integer.toHexString(t[i]);
            hexStr[i] = hexStr[i].toUpperCase();

            if(hexStr[i].length() > 2){
                hexStr[i] = String.valueOf(hexStr[i].charAt(hexStr[i].length()-2)) + hexStr[i].charAt(hexStr[i].length()-1);
            }
            //System.out.println(hexStr[i]);
        }

        return hexStr;
    }

    // key expansion helper function
    private static int toWord(int b1, int b2, int b3, int b4) {
        int word = 0;

        word = word ^ b1 << 24;

        word = word ^ (b2 & 255) << 16;

        word = word ^ (b3 & 255) << 8;

        word = word ^ (b4 & 255);

        return word;
    }

    // key expansion helper function
    private int subWord(int word) {
        int newWord = 0;
        newWord = newWord ^ (int) sboxTransform((byte) (word >>> 24)) & 255;
        newWord <<= 8;

        newWord = newWord ^ (int) sboxTransform((byte) ((word & 0xff0000) >>> 16)) &
                0x000000ff;
        newWord <<= 8;

        newWord = newWord ^ (int) sboxTransform((byte) ((word & 0xff00) >>> 8)) &
                0x000000ff;
        newWord <<= 8;

        newWord = newWord ^ (int) sboxTransform((byte) (word & 0xff)) & 0x000000ff;

        return newWord;
    }

    // key expansion helper function
    private int rotWord(int word) {
        return (word << 8) ^ ((word >> 24) & 255);
    }

    // key expansion helper function
    private static byte sboxTransform(byte value) {
        byte bUpper;
        byte bLower;
        bUpper = (byte) ((byte) (value >> 4) & 0x0f);
        bLower = (byte) (value & 0x0f);
        return sbox[bUpper][bLower];
    }

    // key expansion helper function
    private static byte[][] sbox = { {  (byte) 0x63, (byte) 0x7c, (byte) 0x77, (byte) 0x7b, (byte) 0xf2, (byte) 0x6b,
                                        (byte) 0x6f, (byte) 0xc5, (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2b,
                                        (byte) 0xfe, (byte) 0xd7, (byte) 0xab, (byte) 0x76}, {(byte) 0xca, (byte) 0x82,
                                        (byte) 0xc9, (byte) 0x7d, (byte) 0xfa, (byte) 0x59, (byte) 0x47, (byte) 0xf0,
                                        (byte) 0xad, (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c, (byte) 0xa4,
                                        (byte) 0x72, (byte) 0xc0        },
                                    {   (byte) 0xb7, (byte) 0xfd, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3f,
                                            (byte) 0xf7, (byte) 0xcc, (byte) 0x34, (byte) 0xa5, (byte) 0xe5, (byte) 0xf1,
                                            (byte) 0x71, (byte) 0xd8, (byte) 0x31, (byte) 0x15      },
                                    {   (byte) 0x04, (byte) 0xc7, (byte) 0x23, (byte) 0xc3, (byte) 0x18, (byte) 0x96,
                                            (byte) 0x05, (byte) 0x9a, (byte) 0x07, (byte) 0x12, (byte) 0x80, (byte) 0xe2,
                                            (byte) 0xeb, (byte) 0x27, (byte) 0xb2, (byte) 0x75      },
                                    {   (byte) 0x09, (byte) 0x83, (byte) 0x2c, (byte) 0x1a, (byte) 0x1b, (byte) 0x6e,
                                            (byte) 0x5a, (byte) 0xa0, (byte) 0x52, (byte) 0x3b, (byte) 0xd6, (byte) 0xb3,
                                            (byte) 0x29, (byte) 0xe3, (byte) 0x2f, (byte) 0x84      },
                                    {   (byte) 0x53, (byte) 0xd1, (byte) 0x00, (byte) 0xed, (byte) 0x20, (byte) 0xfc,
                                            (byte) 0xb1, (byte) 0x5b, (byte) 0x6a, (byte) 0xcb, (byte) 0xbe, (byte) 0x39,
                                            (byte) 0x4a, (byte) 0x4c, (byte) 0x58, (byte) 0xcf      },
                                    {   (byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb, (byte) 0x43, (byte) 0x4d,
                                            (byte) 0x33, (byte) 0x85, (byte) 0x45, (byte) 0xf9, (byte) 0x02, (byte) 0x7f,
                                            (byte) 0x50, (byte) 0x3c, (byte) 0x9f, (byte) 0xa8      },
                                    {   (byte) 0x51, (byte) 0xa3, (byte) 0x40, (byte) 0x8f, (byte) 0x92, (byte) 0x9d,
                                            (byte) 0x38, (byte) 0xf5, (byte) 0xbc, (byte) 0xb6, (byte) 0xda, (byte) 0x21,
                                            (byte) 0x10, (byte) 0xff, (byte) 0xf3, (byte) 0xd2      },
                                    {   (byte) 0xcd, (byte) 0x0c, (byte) 0x13, (byte) 0xec, (byte) 0x5f, (byte) 0x97,
                                            (byte) 0x44, (byte) 0x17, (byte) 0xc4, (byte) 0xa7, (byte) 0x7e, (byte) 0x3d,
                                            (byte) 0x64, (byte) 0x5d, (byte) 0x19, (byte) 0x73      },
                                    {   (byte) 0x60, (byte) 0x81, (byte) 0x4f, (byte) 0xdc, (byte) 0x22, (byte) 0x2a,
                                            (byte) 0x90, (byte) 0x88, (byte) 0x46, (byte) 0xee, (byte) 0xb8, (byte) 0x14,
                                            (byte) 0xde, (byte) 0x5e, (byte) 0x0b, (byte) 0xdb      },
                                    {   (byte) 0xe0, (byte) 0x32, (byte) 0x3a, (byte) 0x0a, (byte) 0x49, (byte) 0x06,
                                            (byte) 0x24, (byte) 0x5c, (byte) 0xc2, (byte) 0xd3, (byte) 0xac, (byte) 0x62,
                                            (byte) 0x91, (byte) 0x95, (byte) 0xe4, (byte) 0x79      },
                                    {   (byte) 0xe7, (byte) 0xc8, (byte) 0x37, (byte) 0x6d, (byte) 0x8d, (byte) 0xd5,
                                            (byte) 0x4e, (byte) 0xa9, (byte) 0x6c, (byte) 0x56, (byte) 0xf4, (byte) 0xea,
                                            (byte) 0x65, (byte) 0x7a, (byte) 0xae, (byte) 0x08      },
                                    {   (byte) 0xba, (byte) 0x78, (byte) 0x25, (byte) 0x2e, (byte) 0x1c, (byte) 0xa6,
                                            (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd, (byte) 0x74, (byte) 0x1f,
                                            (byte) 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a      },
                                    {   (byte) 0x70, (byte) 0x3e, (byte) 0xb5, (byte) 0x66, (byte) 0x48, (byte) 0x03,
                                            (byte) 0xf6, (byte) 0x0e, (byte) 0x61, (byte) 0x35, (byte) 0x57, (byte) 0xb9,
                                            (byte) 0x86, (byte) 0xc1, (byte) 0x1d, (byte) 0x9e      },
                                    {   (byte) 0xe1, (byte) 0xf8, (byte) 0x98, (byte) 0x11, (byte) 0x69, (byte) 0xd9,
                                            (byte) 0x8e, (byte) 0x94, (byte) 0x9b, (byte) 0x1e, (byte) 0x87, (byte) 0xe9,
                                            (byte) 0xce, (byte) 0x55, (byte) 0x28, (byte) 0xdf      },
                                    {   (byte) 0x8c, (byte) 0xa1, (byte) 0x89, (byte) 0x0d, (byte) 0xbf, (byte) 0xe6,
                                            (byte) 0x42, (byte) 0x68, (byte) 0x41, (byte) 0x99, (byte) 0x2d, (byte) 0x0f,
                                            (byte) 0xb0, (byte) 0x54, (byte) 0xbb, (byte) 0x16      }
    };

    // key expansion helper array
    private int Rcon[] = {0x01000000, 0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000,
            0x40000000, 0x80000000, 0x1b000000, 0x36000000, 0x6c000000};
}
