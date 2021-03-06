package org.androidtown.wantyou;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyArticleActivity extends AppCompatActivity implements Runnable {

    private ListView likeStudyListView, likeNoticeListView;
    private LikeListAdapter adapter, adapter2;
    private List<Like> likeList, likeList2;
    private JSONArray studyList, noticeList;
    private String message;
    private String domain = "http://172.30.5.56:8080/android/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);

        likeStudyListView = (ListView) findViewById(R.id.likeStudyList);
        likeNoticeListView = (ListView) findViewById(R.id.likeNoticeList);

        likeStudyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Like data = (Like) parent.getItemAtPosition(position);
                Intent selectedStudyIntent = new Intent(MyArticleActivity.this, EditMyStudyActivity.class);
                selectedStudyIntent.putExtra("id", data.getId());
                MyArticleActivity.this.startActivity(selectedStudyIntent);
            }
        });



        likeStudyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemIndex = position;
                final AdapterView adapterView = parent;
                final Like data = (Like) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyArticleActivity.this);
                builder.setTitle("확인");
                builder.setMessage("해당 스터디 항목을 삭제하시겠습니까?\n해당스터와 관련된 모든 사항이 삭제됩니다.");
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        DeleteMyArticleThread deleteMyArticleThread = new DeleteMyArticleThread(MyArticleActivity.this, data.getId());
                        deleteMyArticleThread.start();
                        likeList.remove(itemIndex);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("아니오", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        likeNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Like data = (Like) parent.getItemAtPosition(position);
                Intent selectedNoticeIntent = new Intent(MyArticleActivity.this, EditMyNoticeActivity.class);
                selectedNoticeIntent.putExtra("id", data.getId());
                MyArticleActivity.this.startActivity(selectedNoticeIntent);
            }
        });

        likeNoticeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemIndex = position;
                final AdapterView adapterView = parent;
                final Like data = (Like) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyArticleActivity.this);
                builder.setTitle("확인");
                builder.setMessage("해당 공모전 항목을 삭제하시겠습니까?");
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        DeleteMyArticleThread deleteMyArticleThread = new DeleteMyArticleThread(MyArticleActivity.this, data.getId());
                        deleteMyArticleThread.start();
                        likeList2.remove(itemIndex);
                        adapter2.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("아니오", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        likeList = new ArrayList<Like>();
        likeList2 = new ArrayList<Like>();

        Thread th = new Thread(MyArticleActivity.this);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < studyList.length(); i++) {
            JSONObject order = null;
            try {
                order = studyList.getJSONObject(i);
                likeList.add(new Like(order.getInt("id"), order.getString("title"), order.getString("content"), domain + "getimage?id=" + order.getString("f_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < noticeList.length(); i++) {
            JSONObject order = null;
            try {
                order = noticeList.getJSONObject(i);
                likeList2.add(new Like(order.getInt("id"), order.getString("title"), order.getString("content"), domain + "getimage?id=" + order.getString("f_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new LikeListAdapter(this, likeList);
        adapter2 = new LikeListAdapter(this, likeList2);

        likeStudyListView.setAdapter(adapter);
        likeNoticeListView.setAdapter(adapter2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_my_article);

        likeStudyListView = (ListView) findViewById(R.id.likeStudyList);
        likeNoticeListView = (ListView) findViewById(R.id.likeNoticeList);

        likeStudyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Like data = (Like) parent.getItemAtPosition(position);
                Intent selectedStudyIntent = new Intent(MyArticleActivity.this, EditMyStudyActivity.class);
                selectedStudyIntent.putExtra("id", data.getId());
                MyArticleActivity.this.startActivity(selectedStudyIntent);
            }
        });


        likeStudyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemIndex = position;
                final AdapterView adapterView = parent;
                final Like data = (Like) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyArticleActivity.this);
                builder.setTitle("확인");
                builder.setMessage("해당 스터디 항목을 삭제하시겠습니까?\n해당스터와 관련된 모든 사항이 삭제됩니다.");
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        DeleteMyArticleThread deleteMyArticleThread = new DeleteMyArticleThread(MyArticleActivity.this, data.getId());
                        deleteMyArticleThread.start();
                        likeList.remove(itemIndex);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("아니오", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        likeNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Like data = (Like) parent.getItemAtPosition(position);
                Intent selectedNoticeIntent = new Intent(MyArticleActivity.this, EditMyNoticeActivity.class);
                selectedNoticeIntent.putExtra("id", data.getId());
                MyArticleActivity.this.startActivity(selectedNoticeIntent);
            }
        });

        likeNoticeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemIndex = position;
                final AdapterView adapterView = parent;
                final Like data = (Like) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyArticleActivity.this);
                builder.setTitle("확인");
                builder.setMessage("해당 공모전 항목을 삭제하시겠습니까?");
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        DeleteMyArticleThread deleteMyArticleThread = new DeleteMyArticleThread(MyArticleActivity.this, data.getId());
                        deleteMyArticleThread.start();
                        likeList2.remove(itemIndex);
                        adapter2.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("아니오", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        likeList = new ArrayList<Like>();
        likeList2 = new ArrayList<Like>();

        Thread th = new Thread(MyArticleActivity.this);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < studyList.length(); i++) {
            JSONObject order = null;
            try {
                order = studyList.getJSONObject(i);
                likeList.add(new Like(order.getInt("id"), order.getString("title"), order.getString("content"), domain + "getimage?id=" + order.getString("f_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < noticeList.length(); i++) {
            JSONObject order = null;
            try {
                order = noticeList.getJSONObject(i);
                likeList2.add(new Like(order.getInt("id"), order.getString("title"), order.getString("content"), domain + "getimage?id=" + order.getString("f_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new LikeListAdapter(this, likeList);
        adapter2 = new LikeListAdapter(this, likeList2);

        likeStudyListView.setAdapter(adapter);
        likeNoticeListView.setAdapter(adapter2);

    }

    @Override
    public void run() {
        String url = domain + "myArticle";

        try {
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = org.androidtown.wantyou.HttpClient.getInstance().execute(httpGet);

            String body = EntityUtils.toString(response.getEntity());
            JSONObject obj = new JSONObject(body);

            String result = obj.getString("result");
            String result2 = obj.getString("result2");
            studyList = new JSONArray(result);
            noticeList = new JSONArray(result2);

            if (result.equals("[]") && !result2.equals("[]")) {
                message = "렛츠에 게시한 스터디 목록이 없습니다";
                handler.sendEmptyMessage(0);
            } else if (result2.equals("[]") && !result.equals("[]")) {
                message = "렛츠에 게시한 공모전 목록이 없습니다";
                handler.sendEmptyMessage(0);
            } else if (result.equals("[]") && result2.equals("[]")) {
                message = "렛츠에 게시한 스터디, 공모전 목록이 없습니다";
                handler.sendEmptyMessage(0);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MyArticleActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}
