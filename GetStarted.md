# 介绍 #

JoyHTML的结构简介，欢迎各位修改


# 上手 #

JoyHTML被设计来为Joysearch搜索引擎提取网页中对搜索引擎有价值的信息（有效文本以及链接等等）。

整个项目是一个Netbeans项目，您可以签出代码后可以直接试用Netbeans IDE打开它。当然，如果您愿意使用其它编译器，或者是自己手工查看，编译代码，您可以参考：

### 代码结构: ###
src/java/: 实现代码

src/test/: 测试代码

lib/: 引用库

## 如何使用？ ##

了解JoyHTML，最好的方法是在您的项目中试用它。

以下的代码，可以在您添加了对JoyHTML项目的引用后，起到作用。

```
        HTMLDocument doc = HTMLDocument.createHTMLDocument("http://news.baidu.com", sb.toString());
        for(Anchor a:doc.getAnchors()){
            System.out.println(a.getText()+"   =>   "+a.getURL());
        }
```

您的输出应该类似于：
```
   =>   http://news.baidu.com/view.html
网页   =>   http://www.baidu.com/
贴吧   =>   http://tieba.baidu.com/
知道   =>   http://zhidao.baidu.com/
MP3   =>   http://mp3.baidu.com/
图片   =>   http://image.baidu.com/
视频   =>   http://video.baidu.com/

```
正如您看到的，一个简单的操作，JoyHTML就为您提取出了网页中所有的超链接。

# 想了解更多？ #

我们请看我们提供的[API文档](#.md)。