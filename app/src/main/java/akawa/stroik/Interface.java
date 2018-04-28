package akawa.stroik;

/**
 * Created by Andżej on 2017-06-12.
 */

import java.util.Observable;
import java.util.Observer;

import android.graphics.Color;
import android.util.Log;


public class Interface implements Observer {
    private Stroik tunerActivity;

    private StrunaConfig guitarStringConfig = new StrunaConfig();
    private double frequency;

    String message;

    public Interface(Stroik stroik) {
        tunerActivity = stroik;

    }


    @Override
    public void update(Observable who, Object obj) {
        frequency = (double) obj;
        updateUI();

        Log.e("Freq", "" + frequency);
    }

    private void updateUI() {
        Struna currentString;
        boolean differenceTooHigh = false;
        if (tunerActivity.getTuningType() == 0)
            currentString = guitarStringConfig.getGuitarString(frequency);
        else
            currentString = guitarStringConfig.getGuitarString(tunerActivity.getTuningStringID());

        tunerActivity.changeString(currentString.ID);


        frequency = Math.round(frequency * 10.00) / 10.00;
        if (tunerActivity.getTuningType() == 1 && ((currentString.ID == 1 && frequency > 120)
                || (currentString.ID == 2 && frequency > 140)
                || (currentString.ID == 6 && frequency > 400)
                || (currentString.ID == 5 && frequency > 320)
                || (currentString.ID == 4 && frequency > 260)))
            frequency /= 2;

        if (frequency < currentString.poprawnaCzest || frequency < currentString.poprawnaCzest + 0.15) {
            message = "TUNING LOW";
        } else if (frequency > currentString.poprawnaCzest || frequency > currentString.poprawnaCzest - 0.15) {
            message = "TUNING HIGH";
        } else if (frequency == currentString.poprawnaCzest || frequency <= currentString.poprawnaCzest + 0.15 || frequency >= currentString.poprawnaCzest - 0.15) {
            message = "PERFECT";
        }

        if (tunerActivity.getTuningType() == 1 && frequency < currentString.poprawnaCzest - 30 && frequency != 0) {
            frequency = currentString.poprawnaCzest - 30;
            differenceTooHigh = true;
        }
        if (tunerActivity.getTuningType() == 1 && frequency > currentString.poprawnaCzest + 30) {
            frequency = currentString.poprawnaCzest + 30;
            differenceTooHigh = true;
        }

        if (frequency > 0.0 && frequency < 400) {
            switch (message) {
                case "TUNING LOW": {
                    if (differenceTooHigh) {
                        tunerActivity.updateMessage("   "+currentString.nazwa + "\n"+" " + currentString.poprawnaCzest );
                        tunerActivity.updateMessageT(" " + String.format("%.1f", frequency) + "Hz");
                        tunerActivity.updateMessageI("Za nisko!");
                    }  else {
                        tunerActivity.updateMessage("   "+currentString.nazwa + "\n"+" " + currentString.poprawnaCzest );
                        tunerActivity.updateMessageT(" " + String.format("%.1f", frequency) + "Hz");
                        tunerActivity.updateMessageI("Za nisko!");
                    }

                    break;
                }

                case "TUNING HIGH": {
                    if (differenceTooHigh) {
                        tunerActivity.updateMessage("     "+currentString.nazwa + "\n"+" " + currentString.poprawnaCzest );
                        tunerActivity.updateMessageT(" " + String.format("%.1f", frequency) + "Hz");
                        tunerActivity.updateMessageI("Za wysoko!");
                    }  else {
                        tunerActivity.updateMessage("     "+currentString.nazwa + "\n"+" " + currentString.poprawnaCzest );
                        tunerActivity.updateMessageT(" " + String.format("%.1f", frequency) + "Hz");
                        tunerActivity.updateMessageI("Za wysoko!");
                    }

                    break;
                }

                case "PERFECT": {
                    tunerActivity.updateMessage("     "+currentString.nazwa + "\n"+" " + currentString.poprawnaCzest );
                    tunerActivity.updateMessageT(" " + String.format("%.1f", frequency) + "Hz");
                    tunerActivity.updateMessageI("Nastrojone!");


                    break;
                }

                default:
                    break;
            }
        }
    }


}


