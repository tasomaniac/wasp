package com.orhanobut.waspsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.wasp.Callback;
import com.orhanobut.wasp.Response;
import com.orhanobut.wasp.Wasp;
import com.orhanobut.wasp.WaspError;
import com.orhanobut.wasp.WaspRequest;
import com.orhanobut.wasp.utils.RequestManager;
import com.orhanobut.wasp.utils.SimpleRequestManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@SuppressWarnings("unused")
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private final RequestManager requestManager = new SimpleRequestManager();

  private TextView textView;
  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ListView listView = (ListView) findViewById(R.id.list);

    String[] list = {
        "GET",
        "GET_OBSERVABLE",
        "GET_SYNC",
        "POST",
        "POST_PATH",
        "PUT",
        "PATCH",
        "DELETE",
        "HEAD",
        "FORM_URL_ENCODED",
        "MULTIPART",
        "IMAGE_LISTVIEW",
        "IMAGE_RECYCLERVIEW"
    };

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
        this, android.R.layout.simple_list_item_1, list
    );

    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    String key = (String) parent.getItemAtPosition(position);
    switch (key) {
      case "GET":
        get();
        break;
      case "GET_OBSERVABLE":
        getObservable();
        break;
      case "GET_SYNC":
        getSync();
        break;
      case "POST":
        post();
        break;
      case "POST_PATH":
        postPath();
        break;
      case "PUT":
        put();
        break;
      case "PATCH":
        patch();
        break;
      case "DELETE":
        delete();
        break;
      case "HEAD":
        head();
        break;
      case "FORM_URL_ENCODED":
        formUrlEncoded();
        break;
      case "MULTIPART":
        multipart();
        break;
      case "IMAGE_LISTVIEW":
        startListViewActivity();
        break;
      case "IMAGE_RECYCLERVIEW":
        startRecyclerViewActivity();
        break;
    }
  }

  private final Callback<User> callback = new Callback<User>() {
    @Override
    public void onSuccess(Response response, User user) {
      if (user == null) {
        return;
      }
      showToast(user.toString());
    }

    @Override
    public void onError(WaspError error) {
      showToast(error.getErrorMessage());
    }
  };

  private void multipart() {
    //TODO
  }

  @Override
  protected void onStop() {
    requestManager.cancelAll();
    super.onStop();
  }

  private void formUrlEncoded() {
    getService().postFormUrlEncoded("param1", "param2", callback);
  }

  private void head() {
    getService().head(callback);
  }

  private void delete() {
    getService().delete(callback);
  }

  private void patch() {
    getService().patch(new User("Wasp"), callback);
  }

  private void put() {
    WaspRequest request = getService().put(new User("Wasp"), callback);
    requestManager.addRequest(request);
  }

  private void post() {
    WaspRequest request = getService().post(new User("Wasp"), callback);
    requestManager.addRequest(request);
  }

  private void postPath() {
    WaspRequest request = getService().postPath("2423 234 ", new User("Wasp"), callback);
    requestManager.addRequest(request);
  }

  private void get() {
    WaspRequest request = getService().get(callback);
    requestManager.addRequest(request);
  }

  private void getObservable() {
    getService().getObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<User>() {
          @Override
          public void onCompleted() {
            Log.d("tag", "completed");
          }

          @Override
          public void onError(Throwable e) {
            Log.d("tag", e.getMessage());
          }

          @Override
          public void onNext(User user) {
            Log.d("tag", user.toString());
          }
        });
  }

  private void getSync() {
    User user = getService().getSync();
  }

  private void startListViewActivity() {
    startActivity(ListViewActivity.newIntent(this));
  }

  private void startRecyclerViewActivity() {
    startActivity(RecyclerViewActivity.newIntent(this));
  }
}
