package akawa.stroik;

/**
 * Klasa zajmująca się obsługą bufora przechowującego dane z mikrofonu (próbki)
 *
 * void dodaj - Dodaje próbkę do tablicy
 *
 * int pobierzElement - pobiera próbki sygnału oraz zwraca ilość tych elementów w tablicy
 *
 *
 */

public class Bufor {
    private short [] wartosci;
    private int poczatek;
    private int rozmiar;
    private int koniec;

    public Bufor(int _size) {
        rozmiar = _size;
        wartosci = new short[rozmiar];
        poczatek = 0;
        koniec = 0;
    }

    public synchronized void dodaj(short x) {
        wartosci[poczatek++] = x;
        if(poczatek >= rozmiar) poczatek -= rozmiar;
        koniec = Math.min(koniec +1, rozmiar);
    }

    public synchronized int pobierzElement(double [] wynik, int maxElementow) {
        int toRead = Math.min(maxElementow, koniec);
        int current = poczatek - 1;
        for(int i=toRead-1; i>=0; --i) {
            if(current < 0) current+= rozmiar;
            wynik[i]= wartosci[current--];
        }
        return toRead;
    }
}
