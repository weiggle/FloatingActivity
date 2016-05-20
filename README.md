# 悬浮的Activity

标签（空格分隔）： Android

---
#why?
在做项目的时候，有时候需要做成浮层的样式，类似Dialog、Popupwindow的样式，有时候这两个组件并不能很好的适合项目需求，这个时候一个悬浮的Activity是一个很好的选择，由于是一个Activity，有自己的布局和我们比较熟悉的生命周期，我们可以更好的管理。

#How?
其实做一个悬浮的Activity很简单，
1.新建一个普通的Activity：FloatActivity，编写我们需要的布局文件
2.在style.xml文件新建一个主题：
```
   <style name="Theme.Transparent" parent="Theme.AppCompat.Dialog">
        <item name="android:windowNoTitle">true</item>//对话框无标题
        <item name="android:windowIsTranslucent">true</item>//此对话框的背景
        <item name="android:windowBackground">@android:color/transparent</item>//对话框是否透明
        <item name="android:windowContentOverlay">@null</item>//对话框是否有遮盖
        <item name="android:windowIsFloating">true</item> //对话框是否浮动
        <item name="android:backgroundDimEnabled">true</item>//是否允许对话框的背景变暗
        <item name="android:windowAnimationStyle">@android:style/Animation.Dialog</item>
    </style>
```

# just do it!
1.我的MainAciticvity,只有一个按钮，目的打开FloatingActivity,并带入一些数据，
```
public class MainActivity extends AppCompatActivity {

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FloatActivity.class);
                intent.putExtra("data","i come from MainAcvitity");
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String content = data.getStringExtra("result");
            Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
        }
    }
}
```

我们的FloatActivity：就两个控件，一个TextView用于显示MainActivity传入的数据，一个ListView.
```
public class FloatActivity extends AppCompatActivity {

    private TextView mTextView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        String data = getIntent().getStringExtra("data");
        mTextView = (TextView) findViewById(R.id.txt);
        mTextView.setText(data);

        mListView = (ListView) findViewById(R.id.lv);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mListView.getLayoutParams();
        params.height =(int)(getResources().getDisplayMetrics().heightPixels*2/5);

        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fillDatas());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resultData = mAdapter.getItem(position);
                Intent intent = new Intent(FloatActivity.this,MainActivity.class);
                intent.putExtra("result",resultData);
                setResult(RESULT_OK,intent);
                onBackPressed();
            }
        });

    }
    
    private List<String> fillDatas(){
        List<String> list = new ArrayList<>();
        for(int i = 0;i< 50;i++){
            list.add("this is item "+i);
        }
        return list;
    }
}
这里在mListView的点击事件中，调用了 onBackPressed()方法，这个是让FloatingActivity消失，否则不会消失。

```
2.style.xml
```
<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <style name="Theme.Transparent" parent="Theme.AppCompat.Dialog">
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
    </style>

    <style name="Theme.Transparent.NoActionBar" parent="Theme.AppCompat.Dialog">
        <item name="android:windowNoTitle">true</item>//对话框无标题
        <item name="android:windowIsTranslucent">true</item>//此对话框的背景
        <item name="android:windowBackground">@android:color/transparent</item>//对话框是否透明
        <item name="android:windowContentOverlay">@null</item>//对话框是否有遮盖
        <item name="android:windowIsFloating">true</item> //对话框是否浮动
        <item name="android:backgroundDimEnabled">true</item>//是否允许对话框的背景变暗
        <item name="android:windowAnimationStyle">@android:style/Animation.Dialog</item>
    </style>


</resources>
```
在此处我遇到了一个问题，就是在显示FloatActivity时，总是显示这个项目的名称，即使我在App主题
```
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```
同样会显示这样的问题，
![此处输入图片的描述][1]

  这让我很是苦恼，结果我在FloatActivity的主题上加了一个NoActionBar,就完美的显示了
  
```
  <style name="Theme.Transparent" parent="Theme.AppCompat.Dialog">
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
    </style>
```

3.应用主题

```
  <activity android:name=".FloatActivity"
            android:theme="@style/Theme.Transparent">
        </activity>
```

显示的结果
![此处输入图片的描述][2]


  [1]: http://i2.buimg.com/114fb76f767ccd5f.png
  [2]: http://i4.buimg.com/cd469ecfabcbde8d.png
