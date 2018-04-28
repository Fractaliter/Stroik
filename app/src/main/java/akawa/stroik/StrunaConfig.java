package akawa.stroik;

/**
 * Created by Andżej on 2017-06-12.
 */

/**
 * Klasa tworząca tablice strun np standardowego strojenia
 *
 * dwie metody get guitarstring zwracające obiekt klasy Struna po ID oraz poprzez podanie częstotliwości
 *
 *
 */

public class StrunaConfig {
    private Struna[] struny;


    public StrunaConfig() {
        double[] czestotliwosci = new double[]{82.4, 110.0, 146.3, 195.5, 245.6, 329.6};
        String[] nazwy = new String[]{"E", "A", "D", "G", "B", "e"};
        struny = new Struna[czestotliwosci.length];

        for (int i = 0; i < czestotliwosci.length; ++i) {
            double minCzest, maxCzest;

            if (i != 0) {
                minCzest = (czestotliwosci[i] + czestotliwosci[i - 1]) / 2;
            } else {
                minCzest = 50;
            }

            if (i != czestotliwosci.length - 1) {
                maxCzest = (czestotliwosci[i] + czestotliwosci[i + 1]) / 2;
            } else {
                maxCzest = 400;
            }

            struny[i] = new Struna(i + 1, czestotliwosci[i], minCzest, maxCzest, nazwy[i]);
        }
    }

    Struna getGuitarString(double frequency) {
        for (int i = 0; i < struny.length; ++i) {
            if (frequency >= struny[i].minCzest && frequency <= struny[i].maxCzest)
                return struny[i];
        }
        return new Struna();
    }

    Struna getGuitarString(int id) {
        if (id >= 0 && id < struny.length) return struny[id];
        else return new Struna();
    }
}
