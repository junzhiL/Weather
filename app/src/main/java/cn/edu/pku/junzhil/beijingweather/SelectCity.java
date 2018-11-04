package cn.edu.pku.junzhil.beijingweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;
import java.util.ArrayList;
import cn.edu.pku.junzhil.app.MyApplication;
import cn.edu.pku.junzhil.bean.City;
import cn.edu.pku.junzhil.adapter.CityAdapter;
import cn.edu.pku.junzhil.view.ClearEditText;


public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mList;
    private String cityCode;
    private List<City> cityList;
    private TextView mTodayCity;
    private CityAdapter mAdapter;
    private ClearEditText mClearEditText;
    private List<City> filterDataList;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
        initEditText();
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
        filterDataList = new ArrayList<City>();
        for (City city :cityList){
            filterDataList.add(city);
        }
        mAdapter = new CityAdapter(SelectCity.this,filterDataList);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                City city = cityList.get(i);
                City city = filterDataList.get(i);
                mTodayCity.setText("当前城市：" + city.getCity());
                cityCode = city.getNumber();
            }
        });
    }
    private void initEditText() {
        mClearEditText = (ClearEditText) findViewById(R.id.search_city);
        /* 根据输入框的改变来过滤搜索 */
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /* 当输出框里面的值为空，更新为原来的列表，否则未过滤数据列表 */
                filterData(charSequence.toString());
                mList.setAdapter(mAdapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        //filterDataList = new ArrayList<City>();
        Log.d("Filter", filterStr);
        /* 判断搜索框是否为空，为空则显示全部数据 */
        if (TextUtils.isEmpty(filterStr)) {
            filterDataList.clear();
            for (City city : cityList) {
                filterDataList.add(city);
            }
        } else {
            /* 字符串不为空，则比较搜索框内容和数据库对象的属性进行性检索 */
            filterDataList.clear();
            for (City city : cityList) {
                /* 排除大小写的影响 */
                String allFP_lower = city.getAllFirstPY().toLowerCase();
                String FP_lower = city.getFirstPY().toLowerCase();
                String AP_lower = city.getAllPY().toLowerCase();
                if (city.getAllFirstPY().indexOf(filterStr.toString()) == 0 || city.getFirstPY().indexOf(filterStr.toString()) == 0 || city.getAllPY().indexOf(filterStr.toString()) == 0
                        || city.getNumber().indexOf(filterStr.toString()) == 0 || city.getCity().indexOf(filterStr.toString()) != -1 || allFP_lower.indexOf(filterStr.toString()) == 0
                        || FP_lower.indexOf(filterStr.toString()) == 0 || AP_lower.indexOf(filterStr.toString()) == 0) {
                    filterDataList.add(city);
                }
            }
        }
        //根据a-z进行排序
        //Collections sort(filterDataList, pinyinComparator)
        mAdapter.notifyDataSetChanged();
    }
}
