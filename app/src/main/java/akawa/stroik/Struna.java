package akawa.stroik;

/**
 * Created by Andżej on 2017-06-12.
 */

/**
 * Klasa struny, kazda z nich ma odpowiednia czestotliwosc, id, oraz nazwe
 *
 *
 *
 */

public class Struna{
        public int ID;
        public double poprawnaCzest;
        public double minCzest;
        public double maxCzest;
        public String nazwa;

        public Struna(int _id, double _properFreq, double _minFreq, double _maxFreq, String _name) {
            ID = _id;
            poprawnaCzest = _properFreq;
            minCzest = _minFreq;
            maxCzest = _maxFreq;
            nazwa = _name;
        }

        public Struna() {
            ID = 0;
            poprawnaCzest = 0;
            minCzest = 0;
            maxCzest = 0;
            nazwa = "0";
        }

}