package sample;

import com.google.gson.Gson;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    public Text price1;
    public Text price2;
    public Text price3;
    public Text diffInPercent1;
    public Text change;
    public Text usdChange;
    private double lastValue;

    private static String lastMp3 = "";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static DecimalFormat f = new DecimalFormat("##.00");
    private static OkHttpClient client = new OkHttpClient();
    private double diff = 0.3;

    private double position = 139;
    private double sizeCount = 47.70;
    private double positionSize = position * sizeCount;



    public void initialize() {
        /*
        String webSocketURL = "wss://api.bitfinex.com/ws/2";
        //wscat -c wss://api.bitfinex.com/ws/2
        //{ "event": "subscribe",  "channel": "ticker",  "symbol": "tBTCUSD" }

        */

        String BTCURL = "https://api.bitfinex.com/v1/pubticker/btcusd";
        String ETHURL = "https://api.bitfinex.com/v1/pubticker/ethusd";
        String NEOURL = "https://api.bitfinex.com/v1/pubticker/neousd";

        scheduler.scheduleAtFixedRate(() -> {
            try {
                //String btcResult = getTicker(BTCURL);
                //String neoResult = getTicker(NEOURL);
                //Ticker btcTicker = gson.fromJson(btcResult, Ticker.class);
                //Ticker neoTicker = gson.fromJson(neoResult, Ticker.class);
                //price1.setText(btcTicker.getLastPrice());
                //price3.setText(neoTicker.getLastPrice());
                diffInPercent1.setText("0");

                String ethResult = getTicker(ETHURL);
                Gson gson = new Gson();
                Ticker ethTicker = gson.fromJson(ethResult, Ticker.class);
                double newValue = Double.parseDouble(ethTicker.getLastPrice());
                setPrice(lastValue, newValue);
                setDiff(position, newValue);

                lastValue = newValue;
            } catch (IOException e) {
                price2.setText("ERROR");
                price2.setFill(Color.RED);
            }
        }, 0, 7, TimeUnit.SECONDS); //todo change to WebSockets

    }

    private void setPrice(double lastValue, double newValue) {
        price2.setText(f.format(lastValue));
        if(lastValue > newValue) {
            price2.setFill(Color.RED);
        } else price2.setFill(Color.GREEN);
    }

    private void setDiff(double position, double newValue){
        double diffInPercentsFromPosition = calculateDiff(position, newValue); // разница между текущей позицией и ценой
        if(diffInPercentsFromPosition < 0) {
            diffInPercent1.setText(f.format(diffInPercentsFromPosition));
            diffInPercent1.setFill(Color.RED);
        } else if (diffInPercentsFromPosition > 0){
            diffInPercent1.setText(f.format(diffInPercentsFromPosition));
            diffInPercent1.setFill(Color.GREEN);
        }

        String usdDiff = f.format(positionSize * diffInPercentsFromPosition/100) + "$";
        usdChange.setText(usdDiff);
        if(diffInPercentsFromPosition < 0) {
            usdChange.setFill(Color.RED);
        } else usdChange.setFill(Color.GREEN);

        if (lastValue != 0) { //
            double percentageDiffFromLastValue = calculateDiff(lastValue, newValue); // разница между двумя последними значениями
            percentageDiffFromLastValue = Double.valueOf(f.format(percentageDiffFromLastValue));
            if(percentageDiffFromLastValue < 0) {
                change.setText(String.valueOf(percentageDiffFromLastValue));
                change.setFill(Color.RED);
            } else if(percentageDiffFromLastValue > 0){
                change.setText(String.valueOf(percentageDiffFromLastValue));
                change.setFill(Color.GREEN);
            }

            if (Math.abs(percentageDiffFromLastValue) > diff && (lastValue > newValue)) {
                if(lastMp3.equals("dumpit")){
                    String dumpItAgainPath = "src/main/resources/dumpitagain.mp3";
                    playSound(dumpItAgainPath);
                } else {
                    String dumpItPath = "src/main/resources/dumpit1.mp3";
                    playSound(dumpItPath);
                    lastMp3 = "dumpit";
                }

            } else if (Math.abs(percentageDiffFromLastValue) > diff && (lastValue < newValue)) {
                String pumpItPath = "src/main/resources/pumpit1.mp3";
                playSound(pumpItPath);
                lastMp3 = "pumpit";

            }
        }
    }

    private double calculateDiff(double lastValue, double newValue) {
        return ((lastValue - newValue)/ newValue) * 100;
    }

    private void playSound(String path) {
        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private static String getTicker(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /*private void test1(){
        IchimokuChikouSpanIndicator
    }*/
}
