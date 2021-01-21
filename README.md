＃buildtoolslib

＃自用代码，谨慎引用


####介绍
快速构建项目工具库
部分代码收集于网络，感谢原作者们。
整合工具类的清单：

（一）适配器：
CommAdapter :ListView万能适配器
RCommAdapter ：RcyclerView万能适配器

（二）动画：
TVOffAnimation ：电视机关闭动画效果

（三）网络：
1： okhttp封装 
2:  retrofit2 +Rxjava +OkHttp3 封装

（四）系统相关工具
MacUtils 获取相关信息

（五）工具：
BitmapUtils  图片辅助工具
CrashHadle   全局异常捕捉工具
ExcptionUtils 网络相关异常处理工具
InputMethodUtil 键盘输入相关工具
SPUtils   SP存储工具类
StatusBarUtil     状态栏相关工具
ToastUtils    吐司相关工具
ValidateUtils   输入校验工具类

（六）自定义控件
 ...








。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。


#### 安装教程

How to
To get buildtoolslib into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.futurekang:BuildToolsLib:alpha-1.0'
	}
 


 
 
