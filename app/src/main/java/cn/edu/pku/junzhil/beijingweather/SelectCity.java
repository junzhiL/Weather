package cn.edu.pku.junzhil.beijingweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;
import cn.edu.pku.junzhil.app.MyApplication;
import cn.edu.pku.junzhil.bean.City;
import cn.edu.pku.junzhil.adapter.CityAdapter;

public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mList;
    private String cityCode;
    private List<City> cityList;
    private TextView mTodayCity;
    private CityAdapter mAdapter;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                if (cityCode == null){
                    i.putExtra("cityCode", "101010100");
                }else {
                    i.putExtra("cityCode",cityCode);
                }
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }

    private void initViews(){
        Intent intent = getIntent();
        mTodayCity = (TextView)findViewById(R.id.title_city_name);
        mTodayCity.setText("当前城市：" + intent.getStringExtra("city"));
        //  为返回键声明监听事件
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        mList = (ListView)findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication)getApplication();
        cityList = myApplication.getCityList();
        //for (City city : cityList) {
        //filterDataList
        //}
        mAdapter = new CityAdapter(SelectCity.this, cityList);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                City city = cityList.get(i);
                mTodayCity.setText("当前城市：" + city.getCity());
                cityCode = city.getNumber();
            }
        });
    }
}
