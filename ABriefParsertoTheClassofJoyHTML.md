# 代码结构指南 #

## JoyHTML中的各类剖析 ##

# Anchor类： #
各变量

String URL   存放超链接(该变量不符合JAVA命名规则，有待改进)

String test    该超链接所对应的文本

本类设有 Getters

如：
网页      http://www.baidu.com/

贴吧      http://tieba.baidu.com/


## Document类: ##

String content
以String 形式存放待解析的网页文本，content就是文本内容
采用面向对象思维
本类设有Getters


## Parser类： ##

本类由HTMLDocument调用

本类传递进来的参数是由HTMLDocument提供

通过本类对象调用extractLinks方法后可以得到Anchor的一个Vector集合anchors

(PS:个人觉得用ArrayList更节省空间)


## HTMLDocument extends Document： ##
继承Document；

以面向对象的思维建立一个解析类

URL 用于完善提取出的URL，即修复提取出的子链接

str 即传递待解析的网页文本，String格式

本类构造方法中部调用了Parser类解析

通过调用静态方法createHTMLDocument(String URL, String str)来建立一个对象

该对象建立以后自动生成Anchor的一个Vector集合anchors，可以通过Getters调用

## 工作流程 ##


http://joyhtml.googlecode.com/files/flow.JPG