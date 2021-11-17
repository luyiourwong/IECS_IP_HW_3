# �W�H�W·Ҏ�s ���I3

��״�W110�W��ȵ�1�W�� �W�H�W·Ҏ�s(�YӍ�Tһ)(1424) ���I3

Homework #3: In this program assignment, you will develop in Java a simple Web proxy server, which is also able to cache Web pages. This server will do the following four actions:
1.Accept a GET message from a browser;
2.Forward the GET message to the destination Web server;
3.Receive the HTTP response message from the destination server;
4.Forward the HTTP response message to the browser.

## ���g
NOTE: As explained below, the proxy uses DataInputStreams for processing the replies from servers.
This is because the replies are a mixture of textual and binary data and the only input streams in Java which allow treating both at the same time are DataInputStreams.
To get the code to compile, you must use the -deprecation argument for the compiler as follows:
```
javac -deprecation *.java
```

## ����
```
java ProxyCache [port number]
```

## �yԇ
������\�Wվ
http�W퓡������֡��DƬ���x���λ��POST�yԇ
```
http://www.kingbus.com.tw/
```


## Optional Exercises
When you have finished the basic exercises, you can try the following optional exercises.

### Better Error Handling
��GET��POST��ָ���������
�oЧ�ľWַ���@ʾ404 Not Found�W퓡�

### Support for POST-method
HttpRequest�������POST��������body������
��ProxyCache��handle�е�send request�A�΂�����header֮�ᣬ
������POST��body���ͳ�ȥ��

### Add caching
Cachee������URI��HttpResponse�n����
��ProxyCache��handle�Е����ж�Ո���URI�Ƿ��Е��棬
�е�Ԓֱ�ӌ�Response��body���oclient��

���⽨һ��Cachee�Ǟ��˺��xȡ�̓��棬
���Wַ��Щ�Ǚn�������Ľ��֣�ֱ�Ӵ�Response����Ҫ���xȡ�r�����Q�D�Q��
������⽨��e��URI�Ϳ����S��ȡ���������xȡ�ĕr���x��URI���e���׃����
