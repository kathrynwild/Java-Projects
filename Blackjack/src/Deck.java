/*
 * This takes in an integer and returns the card's face value corresponding to that integer
 *
 * @return String      the face value of corresponding integer
 */
public class Deck {
    public static String getFace(int num) {
        if (num == 0) {
            return (" ___\n| 2  |\n|___|");

        } else if (num == 1) {
            return (" ___\n| 3  |\n|___|");
        }
        else if (num == 2) {
            return (" ___\n| 4  |\n|___|");
        }
        else if (num == 3) {
            return (" ___\n| 5  |\n|___|");
        }
        else if (num == 4) {
            return (" ___\n| 6  |\n|___|");
        }
        else if (num == 5) {
            return (" ___\n| 7  |\n|___|");
        }
        else if (num == 6) {
            return (" ___\n| 8  |\n|___|");
        }
        else if (num == 7) {
            return (" ___\n| 9  |\n|___|");
        }
        else if (num == 8) {
            return (" ___\n| 10 |\n|___|");
        }
        else if (num == 9) {
            return (" ___\n| J  |\n|___|");
        }
        else if (num == 10) {
            return (" ___\n| Q  |\n|___|");
        }
        else if (num == 11) {
            return (" ___\n| K  |\n|___|");
        }
        else{
            return (" ___\n| A  |\n|___|");
        }
    }
}
