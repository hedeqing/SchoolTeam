# SchoolTeam
安卓课设题目--校队

## 配置及功能
环境 Android studio 3.3
 
服务器 django（在我的另外一个repository里面 DjangoServer）

数据库使用的时MySQL

功能实现：

	1)登陆，注册功能实现
	
	2)实现学习，比赛，运动等相关组队并且分类查看
	
	3)阶段比赛推荐（大一，大二，大三，大四），以及查看相关推荐信息
	
	4)用户发布组队信息，添加组队，并可以实现模糊搜索，加入队伍
	
	5)队伍中群聊功能
	
	6)个人信息修改
	
	7）我的组队查看。

## app截图	

app截图
### 登陆注册

![](https://i.imgur.com/0zjcJxm.png)


![](https://i.imgur.com/xpLZPpg.png)

	
### 队伍

![](https://i.imgur.com/W7b8gQK.png)

![](https://i.imgur.com/lGNZC1s.png)


### 群聊

![](https://i.imgur.com/fI6IarA.png)


### 个人信息修改

![](https://i.imgur.com/qv8oVWF.png)



### 学习总结

#### 保存数据到本地代码

    public class SaveUtils {
    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param map map<生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNote(Context context, String filename , Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
        }
        note.commit();
    }

    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为NUll)
     */
    public static String getSettingNote(Context context,String filename ,String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return read.getString(dataname, null);
    }
	}

####  网络申请工具类
	/**
     * 发送post请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return json数据包
     */
    public static String sendPostRequest(String url, String param) {
        PrintWriter printWriter = null;
        StringBuilder result = null;
        String status = null;
        BufferedReader bufferedReader = null;
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();

            // TODO: 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // TODO: 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // TODO: 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(connection.getOutputStream());

            // TODO: 发送请求参数
            // TODO: flush输出流的缓冲
            printWriter.print(param);
            printWriter.flush();
            Log.e(TAG, "sendPostRequest: Post Request Successful");

            // TODO: 定义BufferedReader输入流来读取URL的响应
            result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "sendPostRequest: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

#### Gson解析json数据

#### 外部socket实现群聊功能
 			try {
            client = new WebSocketClient(new URI("ws://192.168.137.1:8000/ws/chat/"+room+"/"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    logger.info("握手成功");
                }
                @Override
                public void onMessage(final String message) {

                    final List<String>  contents = new ArrayList<>();
                    final List<String> rooms = new ArrayList<>();
                    final List<String> fromNames = new ArrayList<>();
                    System.out.println(message);
                    try {
                        final JSONObject jsonObject = new JSONObject(message);
                        Log.d(TAG, "onMessage: 信息:"+jsonObject.getString("message"));
                        Log.d(TAG, "onMessage: 来自于"+jsonObject.getString("fromName"));
                        Log.d(TAG, "onMessage: 房间号"+jsonObject.getString("room"));

                        contents.add(jsonObject.getString("message"));
                        fromNames.add(jsonObject.getString("fromName"));
                        rooms.add(jsonObject.getString("room"));
                        Log.d(TAG, "onMessage: data"+contents.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.setData(contents,fromNames,rooms);
                                lv_chat_dialog.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetInvalidated();
                                lv_chat_dialog.smoothScrollToPosition(chatAdapter.getCount() - 1);//移动到尾部
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (message.equals("over")) {
                        client.close();
                    }
                }
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    logger.info("链接已关闭");
                }

                @Override
                public void onError(Exception ex) {
                    logger.info("发送错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();
        logger.info(String.valueOf(client.getDraft()));
        while (!client.getReadyState().equals(WebSocketClient.READYSTATE.OPEN)) {
            logger.info("正在连接");
        }
    }

#### 这次适配器的使用对于我这个菜鸟来说很是重要

1 适配器的构造函数以及viewholder等等，都是很重要的

2 适配里面可以根据构造函数传递过来的上下文环境启动活动 

3 适配器的监听以及各种逻辑的存放都是很有需要的。

####  这次的课设，遇到的问题

1 碎片放在碎片里面 快速跳转时很容易崩溃，还没解决。

2 XRecyvlerView 含有一个header 能跟随整体布局整体滑动，这是我学到的，