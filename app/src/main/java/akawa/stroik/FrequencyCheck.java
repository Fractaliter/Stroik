package akawa.stroik;


import java.util.Observable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;


/**
 * Created by Andżej on 2017-06-12.
 */

/**
 * Klasa FrequencyCheck zajmuje się wychwytywaniem sygnału/pobieraniem a następnie jego obróbką
 *
 * void startThread - metoda pobierająca próbki poprzez nagrywanie (jakoś tak, musisz to ładnie ująć
 *
 * void startThreadAgain - sprawdza czy program wychwytuje próbki, jeśli nie to wznawia to
 *
 *void onPeriodicNotification - Ustawiane jest w jakim okresie mają być pobierane próbki
 *
 *  void FFTAutocorrelation - Najważniejsza metoda która przetwarza próbki sygnału - opisz tam masz komenty że używane jest to i to itd
 *
 *   double getAverageWavelength - zwraca srednią długość fali
 *
 *    //Odchylenie standardowe dlugosci fali
 double getWavelengthStandardDeviation


 *Szukanie próbek jak najdalszych od sredniej i usuwanie ich
        void removeBadSamples


 *private double getCzest - pobieranie największej częstotliwości z wszystkich próbek
 *
 *
 */


public class FrequencyCheck  extends Observable implements AudioRecord.OnRecordPositionUpdateListener {

    private static final int AUDIO_SAMPLING_RATE = 44100;
    private static int audioDataSize = 7200;

    private static double actualNotifyRate = 0.16;
    private static double slowestNotifyRate = 0.3;
    private static double fastestNotifyRate = 0.10;

    private static final double maxStandardDeviation = 2.0; // maksymalne odchylenie standardowe
    private static final double percentToIgnore = 0.2;


    private AudioRecord Audio;
    private int rozmiarBufora;
    private Bufor audioData;

    private Lock lock;
    private double[] analyzedAudioData;
    DoubleFFT_1D fft;
    private int dlugoscfali;
    private double[] dlugoscFal;
    private int iloscElementow;
    Thread Watek;
    private double czest;


    public FrequencyCheck() {
        rozmiarBufora = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
        Audio = new AudioRecord(MediaRecorder.AudioSource.MIC, AUDIO_SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, rozmiarBufora);

        Audio.setRecordPositionUpdateListener(this);
        Audio.setPositionNotificationPeriod((int) (actualNotifyRate * AUDIO_SAMPLING_RATE));

        analyzedAudioData = new double[4 * audioDataSize];
        dlugoscFal = new double[audioDataSize];
        audioData = new Bufor(audioDataSize);
        lock = new ReentrantLock();
        fft = new DoubleFFT_1D(audioDataSize);

        Audio.startRecording();
        startThread();
    }

    private void startThread() {
        short[] audioDataTemp = new short[audioDataSize];
        ;

        final short[] finalAudioDataTemp = audioDataTemp;
        Watek = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int shortsRead = Audio.read(finalAudioDataTemp, 0, audioDataSize);
                    if (shortsRead < 0) {
                    } else {
                        for (int i = 0; i < shortsRead; ++i) {
                            audioData.dodaj(finalAudioDataTemp[i]);
                        }
                    }
                }
            }
        });
        Watek.start();
    }

    public void startThreadAgain() {
        if (Audio.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            Audio.startRecording();
        }
        if (Watek == null) {
            startThread();
        } else if (!Watek.isAlive()) {
            startThread();
        }
    }


    @Override
    public void onPeriodicNotification(AudioRecord recorder) {
        notifyObservers(czest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!lock.tryLock()) {
                    actualNotifyRate = Math.min(actualNotifyRate, slowestNotifyRate);
                    Audio.setPositionNotificationPeriod((int) (actualNotifyRate * AUDIO_SAMPLING_RATE));
                    return;
                } else {
                    actualNotifyRate = Math.max(actualNotifyRate, fastestNotifyRate);
                    Audio.setPositionNotificationPeriod((int) (actualNotifyRate * AUDIO_SAMPLING_RATE));
                }
                czest = getCzest();

                lock.unlock();
                setChanged();
            }
        }).start();
    }


    private void FFTAutocorrelation() {
        //W oparciu o https://dsp.stackexchange.com/questions/24927/advice-on-autocorrelation-via-fft
        //dodatkowo zastosowalem okno Hanninga

        fft = new DoubleFFT_1D(2 * iloscElementow);

        //Okno Hanna (Hanninga)
        for (int i = iloscElementow - 1; i >= 0; i--) {
            analyzedAudioData[2 * i] = analyzedAudioData[i] * (0.5 * (1.0 - Math.cos(2 * Math.PI * (double) i / (double) (iloscElementow - 1))));
        }
        for (int i = 2 * iloscElementow; i < analyzedAudioData.length; ++i)
            analyzedAudioData[i] = 0;

        //transformata fouriera
        fft.complexForward(analyzedAudioData);

        //zamiana czestotliwosci na power spectral density
        //https://dsp.stackexchange.com/questions/10858/autocorrelation-using-fft-of-power-spectrum
        //re*re + im*im
        for (int i = 0; i < iloscElementow; ++i) {
            analyzedAudioData[2 * i] = (analyzedAudioData[2 * i]) * (analyzedAudioData[2 * i]) + (analyzedAudioData[2 * i + 1]) * (analyzedAudioData[2 * i + 1]);
        }
        for (int i = 2 * iloscElementow; i < analyzedAudioData.length; ++i)
            analyzedAudioData[i] = 0;

        // odwrotna transformata fouriera
        fft.complexInverse(analyzedAudioData, false);

        // bierzemy tylko realna czesc
        for (int i = 0; i < iloscElementow; ++i)
            analyzedAudioData[i] = analyzedAudioData[2 * i];
        for (int i = iloscElementow; i < analyzedAudioData.length; ++i)
            analyzedAudioData[i] = 0;
    }

    double getAverageWavelength() {
        double average = 0;
        for (int i = 0; i < dlugoscfali; ++i)
            average += dlugoscFal[i];
        average /= (double) (dlugoscfali);
        return average;
    }

    //Odchylenie standardowe dlugosci fali
    double getWavelengthStandardDeviation() {
        double variance = 0;
        double average = getAverageWavelength();
        for (int i = 1; i < dlugoscfali; ++i)
            variance += Math.pow(dlugoscFal[i] - average, 2);
        variance /= (double) (dlugoscfali - 1);
        return Math.sqrt(variance);
    }

    //Szukanie próbek jak najdalszych od sredniej i usuwanie ich
    void removeBadSamples() {
        int ignoredSamples = (int) (percentToIgnore * dlugoscfali);
        if (dlugoscfali <= 2) return;
        do {
            double average = getAverageWavelength();

            int worst = 0;
            for (int i = 0; i < dlugoscfali; ++i)
            {
                if (Math.abs(dlugoscFal[i] - average) > Math.abs(dlugoscFal[worst] - average))
                    worst = i;
            }

            dlugoscFal[worst] = dlugoscFal[dlugoscfali - 1];
            --dlugoscfali;
        } while (getWavelengthStandardDeviation() > maxStandardDeviation &&
                ignoredSamples-- > 0 && dlugoscfali > 2);
    }

    private double getCzest() {
        iloscElementow = audioData.pobierzElement(analyzedAudioData, audioDataSize);

        FFTAutocorrelation();

        double maximum = 0;
        for (int i = 1; i < iloscElementow; ++i)
            maximum = Math.max(analyzedAudioData[i], maximum);

        int lastStart = 0;
        dlugoscfali = 0;
        boolean zeroCrossed = true;
        for (int i = 0; i < iloscElementow; ++i) {
            if (analyzedAudioData[i] * analyzedAudioData[i + 1] <= 0) zeroCrossed = true;
            if (zeroCrossed && analyzedAudioData[i] > 0.75 * maximum && analyzedAudioData[i] > analyzedAudioData[i + 1]) {
                if (lastStart != 0)
                    dlugoscFal[dlugoscfali++] = i - lastStart;
                lastStart = i;
                zeroCrossed = false;
                maximum = analyzedAudioData[i];
            }
        }

        removeBadSamples();

        double average = getAverageWavelength();

        double calculatedFrequency = (double) AUDIO_SAMPLING_RATE / average;

        return calculatedFrequency;
    }

    @Override
    public void onMarkerReached(AudioRecord recorder) {
    }
}

