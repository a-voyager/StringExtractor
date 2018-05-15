# String Extractor 插件

![logo.png](http://7xqdz8.com1.z0.glb.clouddn.com/logo.png)

帮助 Android 开发者一键释放字符串资源的 Android Studio 插件，[最新版本 v1.0](https://github.com/a-voyager/StringExtractor/raw/master/release/StringExtractor.zip )

欢迎 Fork & Star

![se_xml_file.gif](http://7xqdz8.com1.z0.glb.clouddn.com/se_xml_file.gif)

## 为什么开发？

在 Android 开发中，常常需要将字符串资源释放到项目的 res/values/strings.xml 文件中，面临：**来回切换文件**、**英文 ID 难取名**、**特定前缀重复工作多**等等的问题，不少 Android 开发者为之苦恼，故开发这样一款一键释放字符串资源的 Android Studio 插件 —— String Extractor

## 有什么用？

String Extractor 以 Android Studio 插件的形式提供，对项目零污染，主要包含以下特性：

（1）批量释放当前文件中的字符串资源

（2）链接翻译 API，自动为字符串资源取英文 ID 名

（3）支持自定义前缀名，便于匹配公司编码规范



## 怎么用？

（1）安装

点击[此处](https://github.com/a-voyager/StringExtractor/raw/master/release/StringExtractor.zip)下载 String Extractor 插件，在 Android Studio 中的 Plugins 页面中选择「Install plugin from disk」从本地安装，之后重启 Android Studio 生效

（2）打开

在包含字符串资源的 Java 文件或 XML 布局文件中，选择主菜单 Refactor -> Extract String 即可打开插件（推荐使用**快捷键 Alt + E**）

![WX20180515-194758@2x.png](http://7xqdz8.com1.z0.glb.clouddn.com/WX20180515-194758@2x.png)

（3）用法

弹窗出现后，可以看到默认生成的字符串资源 ID，之后检查并修改资源 ID 前缀。如果是释放 Java 代码中的字符串，需要再检查并修改生成 Java 代码的模板。最后点击 OK，即可在 对应模块的 strings.xml 中生成字符串资源

![WX20180515-195507@2x.png](http://7xqdz8.com1.z0.glb.clouddn.com/WX20180515-195507@2x.png)

来一个动图展示：

![se_java_file.gif](http://7xqdz8.com1.z0.glb.clouddn.com/se_java_file.gif)



## 还有疑问？

（1）插件有多大？会不会造成 Android Studio 卡顿？

插件大小为 300KB 左右，在性能较差或网络速度较慢的电脑上，可能打开插件会较慢，请耐心等待

（2）字符串资源的 ID 根据什么生成的？

字符串资源 ID 根据「前缀」+「翻译后的原始资源」生成。其中前缀默认为「模块名」+「文件名」，翻译接入的百度翻译 API

（3）使用一段时间后，不能翻译出结果了，为什么？

该插件目前采用的百度翻译免费 API，每月有一定的免费翻译额度限制


## 开源许可
    The MIT License (MIT)
    
    Copyright (c) 2018 WuHaojie
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.