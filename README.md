# 網際網路規約 作業3

逢甲大學110學年度第1學期 網際網路規約(資訊碩一)(1424) 作業3

Homework #3: In this program assignment, you will develop in Java a simple Web proxy server, which is also able to cache Web pages. This server will do the following four actions:
1.Accept a GET message from a browser;
2.Forward the GET message to the destination Web server;
3.Receive the HTTP response message from the destination server;
4.Forward the HTTP response message to the browser.

## 編譯
NOTE: As explained below, the proxy uses DataInputStreams for processing the replies from servers.
This is because the replies are a mixture of textual and binary data and the only input streams in Java which allow treating both at the same time are DataInputStreams.
To get the code to compile, you must use the -deprecation argument for the compiler as follows:
```
javac -deprecation *.java
```

## 執行
```
java ProxyCache [port number]
```

## 測試
國光客運網站
http網頁、有文字、圖片、選擇欄位、POST測試
```
http://www.kingbus.com.tw/
```


## Optional Exercises
When you have finished the basic exercises, you can try the following optional exercises.

### Better Error Handling
非GET和POST的指令會被擋下來，
無效的網址會顯示404 Not Found網頁。

### Support for POST-method
HttpRequest如果遇到POST方法會將body儲存下來，
在ProxyCache的handle中的send request階段傳送完header之後，
接著把POST的body傳送出去。

### Add caching
Cache類別會儲存URI和HttpResponse檔案，
在ProxyCache的handle中會先判定請求的URI是否有暫存，
有的話直接將Response的body傳給client。

另外建一個Cache類別是為了好讀取和儲存，
因為網址有些是檔案命名的禁字，直接存Response就需要在讀取時做名稱轉換，
如果另外建個類別放URI就可以隨便取名，反正讀取的時候讀的URI是裡面的變數。
