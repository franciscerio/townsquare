package com.townsquare.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.townsquare.App;
import com.townsquare.R;
import com.townsquare.ui.main.adapter.ChatRoomAdapter;
import com.townsquare.ui.main.models.Message;

import java.io.StringReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private OkHttpClient client;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ChatRoomAdapter adapter;
    private ArrayList<Message> messages;
    private CompositeDisposable disposable;

    @BindView(R.id.message)
    EditText editText;

    @OnClick(R.id.send)
    public void OnClickSend() {
        String message = editText.getText().toString();
        if (!message.isEmpty()) {
            String newMessage = "{\n" +
                    "  \"content\": \" " + message + " \",\n" +
                    "  \"sender\": \"Francis Gwapo\",\n" +
                    "  \"when\": \"Today\",\n" +
                    "  \"images\": \"http://via.placeholder.com/350x150\",\n" +
                    "  \"isOwner\": \"true\"\n" +
                    "}";
            ws.send(newMessage);
            editText.setText("");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        messages = new ArrayList<>();
        client = new OkHttpClient();
        disposable = new CompositeDisposable();

        adapter = new ChatRoomAdapter(this, messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        start();
    }

    private WebSocket ws;

    private void start() {
        Request request = new Request.Builder().url("wss://echo.websocket.org").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();

        ws.send(sampleOwner);
        ws.send(otherUser);
        ws.send(sampleOwner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void output(final Message message) {
        Log.d(TAG, message.toString());

        disposable.add(Observable.just(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Message>() {
                    @Override
                    public void onNext(Message message) {
                        messages.add(message);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError = " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete ");
                    }
                }));

    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d(TAG, "Websocket = " + webSocket + " Response = " + response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            JsonReader reader = new JsonReader(new StringReader(text.trim()));
            reader.setLenient(true);
            Message message = App.getInstance().getGson().fromJson(text, Message.class);
            Log.d(TAG, "Message " + message);
            output(message);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.e(TAG, "Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.e(TAG, "Error : " + t.getMessage());
        }
    }

    private String otherUser = "{\n" +
            "  \"content\": \"sample content\",\n" +
            "  \"sender\": \"Kiel Gwapo\",\n" +
            "  \"when\": \"Today\",\n" +
            "  \"images\": \"http://via.placeholder.com/350x150\",\n" +
            "  \"isOwner\": \"false\"\n" +
            "}";

    private String sampleOwner = "{\n" +
            "  \"content\": \"sample content\",\n" +
            "  \"sender\": \"Francis Gwapo\",\n" +
            "  \"when\": \"Today\",\n" +
            "  \"images\": \"http://via.placeholder.com/350x150\",\n" +
            "  \"isOwner\": \"true\"\n" +
            "}";
}
